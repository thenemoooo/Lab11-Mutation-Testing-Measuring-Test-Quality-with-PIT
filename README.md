# Лаб 11 — Mutation testing: PIT-ээр тестийн чанараа хэмжих

- **Нэр:** Н.Ням-Од
- **Оюутны код:** B222270805
- **Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба туршилт
- **Төсөл:** `ScholarshipCalculator` — `letterGrade`, `gpa`, `scholarship`

## Орчин

`java -version`:

```text
java version "23.0.2" 2025-01-21
Java(TM) SE Runtime Environment (build 23.0.2+7-58)
Java HotSpot(TM) 64-Bit Server VM (build 23.0.2+7-58, mixed mode, sharing)
```

`mvn -version`:

```text
Apache Maven 3.9.12 (848fbb4bf2d427b72bdb2471c22fced7ebd9a7a1)
Maven home: /opt/homebrew/Cellar/maven/3.9.12/libexec
Java version: 25.0.2, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home
Default locale: en_MN, platform encoding: UTF-8
OS name: "mac os x", version: "26.5", arch: "aarch64", family: "Mac OS X"
```

Төсөл `maven.compiler.release=17`-оор компилддаг; Maven өөрөө JDK 25.0.2 дээр ажилладаг.

## Ажиллуулах команд

```bash
mvn clean verify
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

- JaCoCo HTML тайлан: `target/site/jacoco/index.html`
- PIT HTML тайлан: `target/pit-reports/index.html`

## Эхлэлийн хэмжилт

Starter кодыг өөрчлөхгүйгээр `mvn clean verify` болон PIT-ийн `STRONGER` мутаторуудыг ажиллуулсан үр дүн:

| Хэмжүүр | Хамрагдсан / нийт | Хувь |
|---|---:|---:|
| JaCoCo Instruction | 152 / 152 | 100% |
| JaCoCo Branch | 38 / 41 | 92% |
| JaCoCo Line | 25 / 25 | 100% |
| PIT Mutation Coverage | 17 / 63 | 27% |
| PIT-д амьд үлдсэн мутант | 46 / 63 | 73% |

![Starter JaCoCo тайлан](results/images/jacoco-start.png)

![Starter PIT тайлан](results/images/pit-start.png)

Бодит гаралтууд: `results/mvn-verify-start.txt`, `results/jacoco-start.csv`, `results/pit-start.txt`.

**Ажиглалт.** JaCoCo бүх 25 мөрийг хамрагч тул Line Coverage 100% байхад PIT 63 мутантын зөвхөн 17-ыг агнаж 46 нь амьд үлдсэн нь тестүүд кодыг ажиллуулсан ч буцаасан үр дүн, зааг утг, нөхцөлийн нарийн ялгааг шалгаагүйг харуулж байна. Хамралт бол хүрээгүй кодыг хэмжих хэмжүүр юм бол mutation score тестийн баталгааны чанарыг илүү шууд илрүүлдэг. Тиймээс өндөр line coverage-ийг зөвхөн өөрөө онооны шалгалт гэж үзэх боломжгүй.

## Хэсэг A — Амьд мутантуудын ангилал

Starter PIT тайлангийн `SURVIVED` мутант бүрийг эхний `ScholarshipCalculator.java` мөрөнд нь ангилсан. Ижил мөр дээрх ижил шалтгаантай мутацийг нэг мөрөнд нэгтсэн бөгөөд тоо нь пиксэл биш, PIT-ийн бодит мутацийн тоо. Доорх 15 мөрийн нийлбэр **46**; эдний **1** нь equivalent, **45** нь тест дутуу.

| Мөр | Мутаци (PIT-ийн тайлбар, тоо) | Ангилал | Үндэслэл | Арга хэмжээ |
|---:|---|---|---|---|
| 20 | `changed conditional boundary` ×2 | Тест дутуу | 0 ба 100 зааг утгыг шалгаагүй тул хязгаарыг өөрчлөх мутаци үр дүнг өөрчилж чадсан. | `letterGrade(0)`, `letterGrade(100)` болон 0-аас бага, 100-аас их утгаар яг хаалт байдлыг шалгах. |
| 23 | `replaced return value with ""` ×1; `changed conditional boundary` ×1; нөхцөл → `true` ×1, `false` ×1 | Тест дутуу | `"A"` буцаах утг болон 90-ийн зааг утгыг шалгаагүй тул буцаалт болон нөхцөлийн өөрчлөлт амьд үлдсэн. | 90, 89.99, 95 утгад яг `"A"` буцаагийг `assertEquals`-ээр шалгах. |
| 24 | `replaced return value with ""` ×1; `changed conditional boundary` ×1; нөхцөл → `true` ×1, `false` ×1 | Тест дутуу | `"B"` буцаах утг болон 80-ийн зааг утгыг шалгаагүй тул буцаалт болон нөхцөлийн өөрчлөлт амьд үлдсэн. | 80, 79.99, 89.99 утгад яг `"B"` буцаагийг шалгах. |
| 25 | `replaced return value with ""` ×1; `changed conditional boundary` ×1; нөхцөл → `true` ×1, `false` ×1 | Тест дутуу | `"C"` буцаах утг болон 70-ийн зааг утгыг шалгаагүй тул буцаалт болон нөхцөлийн өөрчлөлт амьд үлдсэн. | 70, 69.99, 79.99 утгад яг `"C"` буцаагийг шалгах. |
| 26 | `replaced return value with ""` ×1; `changed conditional boundary` ×1; нөхцөл → `true` ×1, `false` ×1 | Тест дутуу | `"D"` буцаах утг болон 60-ийн зааг утгыг шалгаагүй тул буцаалт болон нөхцөлийн өөрчлөлт амьд үлдсэн. | 60, 59.99, 69.99 утгад яг `"D"` буцаагийг шалгах. |
| 27 | `replaced return value with ""` ×1 | Тест дутуу | `"F"` буцаах утгыг зөвхөн `null` биш эсэхийг шалгасан тул хоосон мөр буцааж чадсан. | 0 ба 59.99 утгад яг `"F"` буцаагийг шалгах. |
| 36 | `Changed switch default to be first case` ×1 | Equivalent | Java switch-д `case` шошгыг тохирох утга сонгохдоо `default`-ийн байрлалаас хамаарахгүй тул `default`-ыг тэргүйлэх нь зарцмын зан төлөвийг өөрчлөхгүй. | Шинэ тест нэмэх шаардлагагүй; эквивалент гэж үзээд mutation score-оос хасах. |
| 38 | `Replaced double addition with subtraction` ×1 | Тест дутуу | `"B"`-ийн 3 онооны хувь нь зөвхөн GPA-ийн хүрээнд байгаа эсэхийг шалгасан тул хасах үйлдэл амьд үлдсэн. | Дан `"B"` болон олон үсгэн дүний дундажийг яг тоогоор шалгах. |
| 40 | `Replaced double addition with subtraction` ×1 | Тест дутуу | `"D"`-ийн 1 онооны хувь нь зөвхөн GPA-ийн хүрээнд байгаа эсэхийг шалгасан тул хасах үйлдэл амьд үлдсэн. | Дан `"D"` болон олон үсгэн дүний дундажийг яг тоогоор шалгах. |
| 44 | `replaced double return with 0.0d` ×1 | Тест дутуу | Буцаах GPA-ийн яг утгыг шалгаагүй тул бүх утгыг 0 болгосон мутаци хүрээний шалгалтаас өнгөрсөн. | Эерэг GPA-ийн яг утгыг `assertEquals(expected, actual, delta)`-аар шалгах. |
| 48 | `changed conditional boundary` ×2; нөхцөл → `true` ×1 | Тест дутуу | GPA 0/4, кредит 0 болон тэдгээрийн гадна оролтын бүх хослолыг шалгаагүй тул шалгалтын зарим өөрчлөлт амьд үлдсэн. | GPA 0, 4, -0.01, 4.01; кредит 0, -1 утгаар хаалт болон буруу оролтын тест нэмэх. |
| 51 | `removed conditional - replaced equality check with false` ×1, `true` ×1 | Тест дутуу | `hasViolation=true` үед 0 буцаагийг зөвхөн хүрээний хувьд шалгасан тул нөхцөлийг хасах эсвэл бүрдүүлэх мутаци амьд үлдсэн. | Зөрчилгүй үед 100/70/50, зөрчилтэй үед яг 0 буцаагийг тусад нь шалгах. |
| 52 | `replaced int return with 0` ×1; `changed conditional boundary` ×2; нөхцөл → `true` ×2, `false` ×2 | Тест дутуу | 100%-ийн яг утга, GPA 3.8 ба кредит 15 зааг утгыг шалгаагүй тул буцаалт болон хоёр нөхцөлийн өөрчлөлт амьд үлдсэн. | 3.8/15-ийг яг 100 гэж шалгах; GPA 3.79, кредит 14 зэрэг хослол нэмэх. |
| 53 | `replaced int return with 0` ×1; `changed conditional boundary` ×2; нөхцөл → `true` ×2, `false` ×2 | Тест дутуу | 70%-ийн яг утга, GPA 3.4 ба кредит 12 зааг утгыг шалгаагүй тул буцаалт болон хоёр нөхцөлийн өөрчлөлт амьд үлдсэн. | 3.4/12-ийг яг 70 гэж шалгах; GPA 3.39, кредит 11 зэрэг хослол нэмэх. |
| 54 | `replaced int return with 0` ×1; `changed conditional boundary` ×1; нөхцөл → `true` ×1, `false` ×1 | Тест дутуу | 50%-ийн яг утга болон GPA 3.0 зааг утгыг шалгаагүй тул буцаалт болон нөхцөлийн өөрчлөлт амьд үлдсэн. | GPA 3.0-ийг яг 50, 2.99-ийг 0 гэж шалгах. |

`results/a-survivor-count.txt` файлд энэ хүснэгтийн нийлбэрийг Starter PIT тайлангийн 46 амьд мутанттай тулгасан шалгалтыг хадгална.
