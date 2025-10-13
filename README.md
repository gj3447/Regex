# Custom Regex Engine

Java로 구현된 커스텀 정규표현식 엔진입니다. 자체 파서와 AST 기반 인터프리터를 통해 정규표현식 패턴 매칭을 수행합니다.

## 🎯 특징

- **완전한 자체 구현**: Java 표준 라이브러리 regex를 사용하지 않고 처음부터 구현
- **파이프라인 아키텍처**: Tokenization → Lexing → Parsing → Execution
- **백트래킹 지원**: 복잡한 패턴에 대한 자동 백트래킹
- **Longest Match 전략**: 가장 긴 매칭 결과 반환
- **상태머신 기반**: 명확한 상태 전환과 에러 처리

## 🚀 빠른 시작

### 필요 환경

- Java 8 이상
- 컴파일러: `javac`

### 컴파일

```bash
# src 디렉토리에서 컴파일
javac -d ../bin src/com/regex/**/*.java
```

### 실행

```bash
# bin 디렉토리에서 실행
cd bin
java com.regex.main.Main
```

### 입력 파일 설정

`data/regex.txt` 파일에 정규표현식 패턴을 작성:

```
("hello"|"world"){1,2}
```

## 📝 지원하는 문법

| 문법 | 설명 | 예시 |
|------|------|------|
| `"문자열"` | 고정 문자열 매칭 | `"hello"` |
| `(...)` | 그룹화 | `(abc)` |
| `\|` | OR 연산 (선택) | `"a"\|"b"` |
| `[...]` | 문자 클래스 | `["a-z","0-9"]` |
| `~[...]` | NOT 문자 클래스 | `~["0-9"]` |
| `{n,m}` | n~m회 반복 | `"a"{2,5}` |
| `?` | 0 또는 1회 | `"a"?` |
| `+` | 1회 이상 | `"a"+` |
| `*` | 0회 이상 | `"a"*` |

## 💡 사용 예시

### 예시 1: 이메일 도메인 매칭

```
regex.txt: ("@"["a-z","A-Z"]+".com")
입력: "user@example.com"
결과: "@example.com"
```

### 예시 2: 선택 매칭

```
regex.txt: ("hello"|"world")
입력: "hello world"
결과: "hello"
```

### 예시 3: 반복 패턴

```
regex.txt: ["a-z"]{3,5}
입력: "test1234"
결과: "test"
```

## 🏗️ 아키텍처

프로젝트는 명확한 계층 구조로 설계되어 있습니다:

```
┌─────────────────────────┐
│   Application Layer     │  Main.java
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│   Execution Layer       │  RootNode, Branch
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│    AST Layer            │  Node, And, Or, Repeat
└────────────┬────────────┘  FString, CharClass
             │
┌────────────▼────────────┐
│    Parser Layer         │  Parser
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│  Lexical Analysis       │  Lexer, Lex
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│  Tokenization Layer     │  Tokenizer, Token
└─────────────────────────┘
```

자세한 아키텍처 설명은 [Architecture.md](Architecture.md)를 참고하세요.

## 📂 프로젝트 구조

```
Regex/
├── src/com/regex/
│   ├── main/           # 메인 진입점 및 유틸리티
│   ├── token/          # 토큰화 레이어
│   ├── lexer/          # 어휘 분석 레이어
│   ├── parser/         # 파싱 레이어
│   └── ast/            # AST 및 실행 레이어
├── data/
│   ├── regex.txt       # 정규표현식 패턴 입력
│   ├── string.txt      # 테스트 문자열 입력
│   └── test.txt        # 추가 테스트 데이터
├── bin/                # 컴파일된 클래스 파일
├── Architecture.md     # 상세 아키텍처 문서
└── README.md           # 이 파일
```

## 🔧 핵심 컴포넌트

### 1. Tokenizer
문자열을 의미 있는 토큰으로 분해합니다.
- **패턴**: State Machine Pattern
- **입력**: `String` (정규표현식 패턴)
- **출력**: `ArrayList<Token>`

### 2. Lexer
토큰을 구조적 의미를 가진 렉스(Lexeme)로 그룹화합니다.
- **패턴**: Stack-based Parser
- **입력**: `ArrayList<Token>`
- **출력**: `ArrayList<Lex>`

### 3. Parser
렉스 시퀀스를 추상 구문 트리(AST)로 변환합니다.
- **패턴**: Builder Pattern
- **입력**: `ArrayList<Lex>`
- **출력**: `RootNode` (AST)

### 4. AST Executor
AST를 해석 실행하여 문자열 매칭을 수행합니다.
- **패턴**: Interpreter Pattern, Composite Pattern
- **입력**: `String` (매칭할 문자열)
- **출력**: `String` (매칭된 부분 문자열)

## 🎓 학습 가치

이 프로젝트는 다음과 같은 컴파일러/인터프리터 이론을 실습할 수 있습니다:

- **Lexical Analysis (어휘 분석)**: 문자 스트림을 토큰으로 변환
- **Syntax Analysis (구문 분석)**: 토큰을 구조화된 트리로 변환
- **Abstract Syntax Tree**: 프로그램 구조의 트리 표현
- **Interpreter Pattern**: AST 직접 실행
- **Backtracking**: 탐색 실패 시 이전 상태로 복귀
- **State Machine**: 상태 기반 처리 로직

## 🔍 코드 예시

### Main.java
```java
String regex = Func.file_input("regex.txt");
ArrayList<Token> token_list = Tokenizer.string2tokenlist(regex);

Lexer lexer = new Lexer();
ArrayList<Lex> lex_list = lexer.tokenlist2lexlist(token_list);

Parser parser = new Parser();
RootNode root = parser.lexlist2tree(lex_list);

String result = root.run("hello world test");
System.out.println(result);
```

## 🐛 디버깅

각 단계별 출력을 확인하여 디버깅할 수 있습니다:

```java
// 토큰 출력
for(String s : Tokenizer.tokenlist2stringlist(token_list)) {
    System.out.println(s);
}

// 렉스 출력
for(Lex l : lex_list) {
    System.out.println(l.getString());
}

// AST 출력
root.print();
```

## 📊 실행 흐름

1. **파일 읽기**: `regex.txt`에서 패턴 로드
2. **Tokenization**: 문자열 → 토큰 리스트
3. **Lexing**: 토큰 리스트 → 렉스 리스트
4. **Parsing**: 렉스 리스트 → AST
5. **Execution**: 입력 문자열의 모든 위치에서 AST 실행
6. **결과 반환**: 가장 긴 매칭 문자열 반환

## 🤝 기여

이 프로젝트는 학습 목적으로 제작되었습니다. 개선 사항이나 버그를 발견하시면 이슈를 등록해주세요.

## 📄 라이선스

이 프로젝트는 교육 목적으로 제작되었습니다.

## 📚 참고 자료

- [Architecture.md](Architecture.md) - 상세한 시스템 아키텍처 설명
- [seminar.pdf](seminar.pdf) - 프로젝트 세미나 자료

---

**작성일**: 2025-10-13  
**프로젝트**: Custom Regex Engine (Java)

