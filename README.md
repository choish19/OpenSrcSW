# 2022 오픈소스 SW 수업자료

2022-1학기 건국대학교 오픈소스 SW 수업에서 사용하는 클래스 구조의 샘플 파일입니다.

**kuir.java**가 프로젝트의 메인 소스 코드로 사용되고,

주차별로 생성된 **makeCollection.java**, **makeKeyword.java** 파일을 메인 함수의 인자값(String[] args)에 따라 객체를 생성하고 함수를 실행합니다.

## 파일 구조

```bash
├── README.md
├── bin
│   └── scripts
├── jars
└── src
    └── scripts
        ├── kuir.java
        ├── makeCollection.java
        └── makeKeyword.java
``` 

## 인코딩

**Encoding : UTF-8**

## 디렉토리 설명

**src/scrips** : .java 소스 파일이 저장되는 디렉토리

**bin/** : 컴파일된 .class 바이너리 파일이 저장되는 디렉토리

**jars/** : 외부 jar 파일이 저장되는 디렉토리

## 컴파일 명령어

### MAC

`javac -cp (외부 jar 파일 이름 1):(외부 jar 파일 이름 2):,,,, src/scripts/*.java -d bin (-encoding UTF8)`

ex) `javac -cp jars/jsoup-1.13.1.jar:jars/kkma-2.1.jar src/scripts/*.java -d bin -encoding UTF8`

### WINDOWS

`javac -cp (외부 jar 파일 이름 1):(외부 jar 파일 이름 2):,,,, src/scripts/*.java -d bin (-encoding UTF8)`

ex) `javac -cp jars/jsoup-1.13.1.jar:jars/kkma-2.1.jar src/scripts/*.java -d bin -encoding UTF8`

`javac -cp "(외부 jar 파일 이름 1);(외부 jar 파일 이름 2);,,,," src/scripts/*.java -d bin (-encoding UTF8)`

ex) `javac -cp "jars/jsoup-1.13.1.jar;jars/kkma-2.1.jar" src/scripts/*.java -d bin -encoding UTF8`

## 실행 명령어

### MAC

`java -cp (외부 jar 파일 이름 1):(외부 jar 파일 이름 2):,,,,:bin scripts.kuir (args 1) (args 2) ,,, (args n)`

ex) `java -cp ./jars/jsoup-1.13.1.jar:./jars/kkma-2.1.jar:bin scripts.kuir -c data`

### WINDOWS

`java -cp (외부 jar 파일 이름 1);(외부 jar 파일 이름 2);,,,,;bin scripts.kuir (args 1) (args 2) ,,, (args n)`

ex) `java -cp ./jars/jsoup-1.13.1.jar;./jars/kkma-2.1.jar;bin scripts.kuir -c data`
