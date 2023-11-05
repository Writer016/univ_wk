## 폴더 FastNote

### 소스코드 경로
FastNote/app/src/main/java/com/appl/fastnote

### 리팩토링 위치-1
- 리팩토링 필요 로직: << 메모 리스트 가져오기 LOGIC >>, << 가져온 리스트 정렬 LOGIC >>

- 관련 유의점
1) << 메모 리스트 가져오기 LOGIC >>과 << 가져온 리스트 정렬 LOGIC >>은 클래스 NormalFragment와 ProfileFragment 그리고 NormalAdapter와 ProfileAdapter에서 사용됨.
2) << 가져온 리스트 정렬 LOGIC >>은 Normal과 Profile끼리 로직이 완전히 동일.
3) << 메모 리스트 가져오기 LOGIC >>은 Normal과 Profile끼리 로직이 다름.

### 리팩토링 위치-2
- 리팩토링 필요 로직: << 파일 저장 LOGIC >>, << 파일 이름 생성 LOGIC >>

- 관련 유의점
1) << 파일 저장 LOGIC >>과 << 파일 이름 생성 LOGIC >>은 클래스 WriteActivityNormal과 WriteActivityProfile에서 사용됨.
2) << 파일 저장 LOGIC >>과 << 파일 이름 생성 LOGIC >>모두 Normal과 Profile끼리 로직이 다름.

## 폴더 FastNote_ref_1

### FileReader.kt
리팩토링 위치-1에 대한 리팩토링 초안.
### Saver.kt
리팩토링 위치-2에 대한 리팩토링 초안.

## 폴더 FastNote_Refactored
### /loc1
위치-1에 대한 리팩토링 코드. << 메모 리스트 가져오기 LOGIC >>, << 가져온 리스트 정렬 LOGIC >>을 상위 클래스인 FileReader에서 구현함.
로직에 포함되는 파일 형식("n.txt"를 비롯한)은 하위 클래스인 NormalFileReader와 ProfileFileReader로 확장 구현.

#### 리팩토링 전 클라이언트 코드 -- NormalFragment
![image](https://github.com/Writer016/univ_wk/assets/129999079/6ea6913d-0b18-4817-9b05-4ef5a9a017b7)

#### 리팩토링 후 클라이언트 코드 -- NormalFragment
![image](https://github.com/Writer016/univ_wk/assets/129999079/da53efe9-5614-493b-9535-552ccd8e90c9)
