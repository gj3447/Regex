# Regex Engine Architecture

## 📋 시스템 개요

### 목적
정규표현식 패턴을 해석하고 문자열 매칭을 수행하는 커스텀 정규표현식 엔진

### 핵심 기능
- 정규표현식 패턴 파싱 및 검증
- 패턴 기반 문자열 매칭
- 백트래킹을 통한 복잡한 패턴 처리
- Longest Match 전략

### 아키텍처 스타일
- **Pipeline Architecture**: 데이터가 여러 단계를 순차적으로 통과
- **Interpreter Pattern**: AST를 통한 패턴 해석 실행
- **Layered Architecture**: 명확한 계층 분리

---

## 🏗️ 아키텍처 개요

### 계층 구조 (Layered Architecture)

```
┌─────────────────────────────────────────┐
│        Application Layer                │  Main.java
│  (사용자 인터페이스 및 파일 I/O)          │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│      Execution Layer (Runtime)          │  RootNode, Branch
│    (패턴 매칭 실행 및 백트래킹)          │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│     Abstract Syntax Tree Layer          │  Node, And, Or, Repeat
│       (패턴 구조의 트리 표현)            │  FString, CharClass
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│        Parser Layer                     │  Parser
│     (구문 분석 및 트리 생성)             │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│        Lexical Analysis Layer           │  Lexer, Lex
│    (토큰 그룹화 및 구조 분석)            │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│        Tokenization Layer               │  Tokenizer, Token
│      (문자열을 토큰으로 분해)            │
└─────────────────────────────────────────┘
```

### 데이터 변환 파이프라인

```
String Pattern
    ↓ Tokenization
List<Token>
    ↓ Lexical Analysis
List<Lex>
    ↓ Parsing
AST (Tree)
    ↓ Execution
Matched String
```

---

## 🔧 핵심 컴포넌트

### 1. Tokenization Layer

**책임**: 원시 문자열을 의미 단위 토큰으로 분해

**핵심 컴포넌트**:
- `Tokenizer`: 상태머신 기반 토큰화 엔진
- `Token`: 토큰 표현 클래스
- `TokenData`: 복합 토큰 데이터 (STRING, NUMBER)
- `eToken`: 토큰 타입 열거형

**설계 패턴**: State Machine Pattern

**주요 토큰 타입**

| 토큰 | 기호 | 설명 |
|------|------|------|
| `LEFT_PARENTHESIS` | `(` | 그룹 시작 |
| `RIGHT_PARENTHESIS` | `)` | 그룹 끝 |
| `LEFT_BRACKET` | `[` | 문자 클래스 시작 |
| `RIGHT_BRACKET` | `]` | 문자 클래스 끝 |
| `LEFT_BRACE` | `{` | 반복 시작 |
| `RIGHT_BRACE` | `}` | 반복 끝 |
| `VERTICAL_BAR` | `|` | OR 연산자 |
| `QUOTATION_MARK` | `"` | 문자열 리터럴 |
| `TILDE` | `~` | NOT 연산자 |
| `QUESTION_MARK` | `?` | 0~1회 반복 |
| `PLUS_SIGN` | `+` | 1회 이상 반복 |
| `ASTERISK` | `*` | 0회 이상 반복 |
| `COMMA` | `,` | 구분자 |
| `HYPHEN` | `-` | 범위 지정 |
| `BACKSLASH` | `\` | 이스케이프 |
| `NUMBER` | `0-9` | 숫자 |
| `STRING` | 문자 | 일반 문자 |

**상태 전환**:
- `READ`: 단일 토큰 즉시 처리
- `WRITE`: 버퍼 누적
- `MOVE`: 버퍼 플러시
- `FUNC`: 새 컨텍스트 시작

---

### 2. Lexical Analysis Layer

**책임**: 토큰 스트림을 구조적 의미를 가진 렉스(Lexeme)로 그룹화

**핵심 컴포넌트**:
- `Lexer`: 토큰 그룹화 및 구조 분석 엔진
- `Lex`: 렉스 기본 클래스 (추상)
- `lFString`: 고정 문자열 렉스
- `lCharClass`: 문자 클래스 렉스
- `lRepeat`: 반복 구조 렉스
- `eLex`: 렉스 타입 열거형

**설계 패턴**: Stack-based Parser, Template Method Pattern

**자료구조**:
- `lex_stack`: 중첩 구조 추적 (괄호 매칭)
- `token_buffer`: 현재 처리 중인 토큰 버퍼
- `lex_buffer`: 렉스 구성 버퍼

**렉스 타입 계층**

| 렉스 타입 | 설명 | 예시 |
|-----------|------|------|
| `AND_OPEN/CLOSE` | 연속 매칭 그룹 | `(abc)` |
| `OR_OPEN/CLOSE` | 선택 매칭 그룹 | `a|b` |
| `REPEAT_OPEN/CLOSE` | 반복 구조 | `{2,5}`, `?`, `+`, `*` |
| `FSTRING` | 고정 문자열 | `"hello"` |
| `CHARCLASS` | 문자 클래스 | `[a-z]`, `~[0-9]` |

**구조 변환 예시**:
- `"hello"` → `lFString("hello")`
- `[a-z0-9]` → `lCharClass([a-z], [0-9])`
- `a{2,5}` → `REPEAT_OPEN + a + REPEAT_CLOSE`
- `a|b` → `OR_OPEN + AND_OPEN + a + AND_CLOSE + AND_OPEN + b + AND_CLOSE + OR_CLOSE`

---

### 3. Parser Layer

**책임**: 렉스 시퀀스를 추상 구문 트리(AST)로 변환

**핵심 컴포넌트**:
- `Parser`: 구문 분석 및 트리 빌더

**설계 패턴**: Builder Pattern, Stack-based Tree Construction

**자료구조**:
- `node_stack`: 현재 빌드 중인 노드 컨텍스트 스택

**파싱 상태**:
- `FUNC`: 새 컨테이너 노드 생성 (스택 푸시)
- `READ`: 리프 노드 추가
- `BREAK`: 컨테이너 닫기 (스택 팝)

---

### 4. Abstract Syntax Tree (AST) Layer

**책임**: 정규표현식 패턴의 구조적 표현

**노드 계층 구조**:

```
Node (abstract)
├── RootNode
├── And (Container)
├── Or (Container)
├── Repeat (Container)
├── FString (Leaf)
└── CharClass (Leaf)
```

**컨테이너 노드** (Composite Pattern):
- `RootNode`: 최상위 루트, 실행 엔트리포인트
- `And`: 연속 매칭 (Sequence)
- `Or`: 선택 매칭 (Alternation)
- `Repeat`: 반복 매칭 (Quantifier)

**리프 노드** (Primitive):
- `FString`: 고정 문자열 리터럴
- `CharClass`: 문자 클래스 및 범위 (CharRange 포함)

**보조 컴포넌트**:
- `CharRange`: 문자 범위 표현 (예: a-z)
- `Branch`: 백트래킹 분기점 저장
- `eRes`: 실행 결과 타입

---

### 5. Execution Layer (Runtime)

**책임**: AST 해석 실행 및 문자열 매칭

**핵심 메커니즘**:

**Request/Response 패턴**:
```
req(root, index) → 하향 매칭 요청
    ↓
res(root, index, number, type) → 상향 결과 전달
```

**백트래킹 시스템**:
- `branch_stack`: 분기점 스택 (Stack of Choices)
- 실패 시 자동 복귀 및 대안 시도

**실행 전략**:
- **Longest Match**: 모든 시작 위치에서 매칭 시도, 가장 긴 결과 반환
- **Greedy Matching**: 반복 구조에서 최대한 많이 매칭
- **Backtracking**: 실패 시 이전 선택지로 복귀

**응답 타입 (eRes)**:
- `COMPLETE`: 매칭 성공
- `BREAK`: 백트래킹 요청
- `ERROR`: 복구 불가능한 실패

---

## 📊 전체 데이터 흐름 예시

### 입력 정규표현식: `"hello"|"world"`

**1. Tokenizing:**
```
" h e l l o " | " w o r l d "
↓
[QUOTATION, STRING("hello"), QUOTATION, VERTICAL_BAR, QUOTATION, STRING("world"), QUOTATION]
```

**2. Lexing:**
```
[QUOTATION, STRING("hello"), QUOTATION, VERTICAL_BAR, QUOTATION, STRING("world"), QUOTATION]
↓
[AND_OPEN, OR_OPEN, AND_OPEN, FSTRING("hello"), AND_CLOSE, AND_OPEN, FSTRING("world"), AND_CLOSE, OR_CLOSE, AND_CLOSE]
```

**3. Parsing:**
```
[AND_OPEN, OR_OPEN, AND_OPEN, FSTRING("hello"), AND_CLOSE, AND_OPEN, FSTRING("world"), AND_CLOSE, OR_CLOSE, AND_CLOSE]
↓
RootNode
└─ And
   └─ Or
      ├─ And
      │  └─ FString("hello")
      └─ And
         └─ FString("world")
```

**4. Execution:**
```
입력: "hello world"
index=0: "hello" 매칭 성공 → result = "hello"
index=1: 매칭 실패
...
index=6: "world" 매칭 성공 → result = "world"
...
최종 결과: "hello" (더 긴 매칭이 없으면)
```

---

## 🎯 핵심 설계 특징

### 1. 상태머신 기반 처리
- Tokenizer: 문자 단위 상태머신
- Lexer: 토큰 단위 상태머신
- 명확한 상태 전환 규칙

### 2. 스택 기반 구조 분석
- Lexer: 중첩 구조 추적 (괄호 매칭)
- Parser: 트리 생성 시 부모 노드 추적

### 3. 재귀적 req/res 패턴
- 트리 구조를 따라 재귀적 매칭
- 상향식 결과 전파

### 4. 백트래킹 메커니즘
- Branch Stack으로 선택지 관리
- 실패 시 자동으로 이전 선택지 복귀

### 5. Longest Match 전략
- 입력 문자열의 모든 위치에서 매칭 시도
- 가장 긴 매칭 결과 반환

---

## 🗂️ 프로젝트 구조

```
E:\CD\RESEARCH\Regex\
├── src/com/regex/
│   ├── main/
│   │   ├── Main.java          # 진입점, 전체 파이프라인 실행
│   │   ├── Func.java          # 유틸리티 함수
│   │   ├── State.java         # 상태 enum
│   │   └── Regex.java         # (비어있음)
│   │
│   ├── token/
│   │   ├── Tokenizer.java     # 문자열 → 토큰 리스트
│   │   ├── Token.java         # 토큰 클래스
│   │   ├── TokenData.java     # 토큰 데이터 (STRING, NUMBER)
│   │   ├── TokenFunc.java     # 토큰 유틸리티
│   │   └── eToken.java        # 토큰 타입 enum
│   │
│   ├── lexer/
│   │   ├── Lexer.java         # 토큰 리스트 → 렉스 리스트
│   │   ├── Lex.java           # 렉스 기본 클래스
│   │   ├── lFString.java      # 고정 문자열 렉스
│   │   ├── lCharClass.java    # 문자 클래스 렉스
│   │   ├── lRepeat.java       # 반복 렉스
│   │   ├── LexFunc.java       # 렉스 유틸리티
│   │   └── eLex.java          # 렉스 타입 enum
│   │
│   ├── parser/
│   │   └── Parser.java        # 렉스 리스트 → AST
│   │
│   └── ast/
│       ├── Node.java          # AST 노드 기본 클래스
│       ├── RootNode.java      # 루트 노드 (실행 엔진)
│       ├── And.java           # 연속 매칭 노드
│       ├── Or.java            # 선택 매칭 노드
│       ├── Repeat.java        # 반복 노드
│       ├── FString.java       # 고정 문자열 노드
│       ├── CharClass.java     # 문자 클래스 노드
│       ├── CharRange.java     # 문자 범위
│       ├── Branch.java        # 백트래킹용 분기점
│       └── eRes.java          # 응답 타입 enum
│
├── data/
│   ├── regex.txt              # 정규표현식 패턴 입력
│   ├── string.txt             # 매칭할 문자열 입력
│   └── test.txt               # 테스트 데이터
│
└── bin/                       # 컴파일된 .class 파일들
```

---

## 🔧 지원하는 정규표현식 문법

| 문법 | 설명 | 예시 |
|------|------|------|
| `"문자열"` | 고정 문자열 매칭 | `"hello"` |
| `(...)` | 그룹화 | `(abc)` |
| `\|` | OR 연산 | `"a"\|"b"` |
| `[...]` | 문자 클래스 | `["a-z","0-9"]` |
| `~[...]` | NOT 문자 클래스 | `~["0-9"]` |
| `{n,m}` | n~m회 반복 | `"a"{2,5}` |
| `?` | 0~1회 | `"a"?` |
| `+` | 1회 이상 | `"a"+` |
| `*` | 0회 이상 | `"a"*` |

---

## 📝 사용 예시

**regex.txt:**
```
("hello"|"world"){1,2}
```

**실행 결과:**
```
입력: "hello world"
→ "hello" 매칭
```

**Main.java 실행:**
```java
String regex = Func.file_input("regex.txt");
ArrayList<Token> token_list = Tokenizer.string2tokenlist(regex);
ArrayList<Lex> lex_list = lexer.tokenlist2lexlist(token_list);
RootNode root = parser.lexlist2tree(lex_list);
String result = root.run("hello world");
System.out.println(result);  // "hello"
```

---

## 🎓 학습 포인트

이 프로젝트는 다음과 같은 컴파일러/인터프리터 기법을 사용합니다:

1. **Tokenization (어휘 분석)**: 문자열을 의미 단위로 분해
2. **Lexical Analysis (어휘 분석)**: 토큰을 더 큰 의미 단위로 그룹화
3. **Parsing (구문 분석)**: 선형 구조를 트리 구조로 변환
4. **AST (추상 구문 트리)**: 프로그램 구조를 트리로 표현
5. **Interpretation (해석 실행)**: AST를 직접 실행
6. **Backtracking (역추적)**: 탐색 실패 시 이전 상태로 복귀

---

**작성일:** 2025-10-13  
**프로젝트:** Custom Regex Engine (Java)  
**분석자:** AI Assistant



