![logo](https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/fefc4916-535c-4d4a-8451-74bcb9477598)

---  
<br>

<div align="center">
✨✨🏅✨✨ <br>
🏆 2023 캡스톤디자인과창업프로젝트 최우수상 (주관: 이화여자대학교 컴퓨터공학전공 학과장)<br>
🏆 2023 캡스톤디자인 경진대회 은상 (주관: 이화여자대학교 공학교육혁신센터)<br>
</div>


---  
> 목차  
> - [📌 프로젝트 소개](#프로젝트-소개) 
> - [✏️ 주요 기능](#주요-기능)
> - [📜 ERD](#erd)
> - [📄 API 모아보기](#api)
> - [🖥️ 기술 스택](#기술-스택)
> - [📁 파일 구조](#파일-구조)
> - [🚩 시작 가이드](#시작-가이드)
> - [ 🚦프로젝트 관리](#Project-Management)
> - [ 📦 프로젝트 문서](#Project-Documents-&-Links)

<br>  
  
## 프로젝트 소개  

미술관에 갔을 때 작품을 잘 감상하려면 어떻게 해야 할까요? <br>국립중앙박물관을 간다는 외국인 친구에게 무엇을 추천하면 좋을까요? <br><br> 1️⃣ 너무 긴 도슨트의 설명, 내가 원하는 부분만 듣고 싶다면 <br> 2️⃣ 줄글로 된 해설보단 해설과 그림을 함께 보고 싶다면 <br> 3️⃣ 어두운 곳에서 긴 글을 읽기보다 핵심 요소마다 나뉜 해설을 음성으로 편하게 듣고 싶다면 <br><br> 아트 헌터와 함께 미술관에 방문해 보세요! <br><br> 걸려있는 그림들과 해설을 눈으로 훑으며 지나가기 보다는 직접 부분들을 찾고, 해설을 모으면서 작품을 마주한 순간을 기록해 보세요. 보다 즐겁고 자세하게 작품을 이해하고 있는 여러분을 발견하게 될 거예요. <br><br>

### 개발 동기 및 목적  
한국의 문화와 역사에 친숙하지 않은 사용자도 **인터렉션**을 통해 쉽고 재미있게 한국미술을 감상하도록 합니다.  
    
<br>  
<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/b69a1ca2-8afd-4770-ba5d-427f51bce27e" width="800">
<br>  

### 서비스 소개  
  
**1. 🎨 OCR을 통해 작품 캡션 촬영만으로 작품 검색**  
  
- 직접 작품명을 정확하게 타이핑하거나 검색할 필요 없이 간단하게 사진 촬영만으로 원하는 작품의 해설을 감상할 수 있습니다.  
  
**2. 🔎 YOLO를 이용한 작품 요소 인식**  
- 한 작품에서 주목하면 좋을 부분들을 표시해둔 뒤 사용자가 해당 부분을 찾으며 더 자세히 살펴보도록 유도합니다. 유저가 작품의 부분을 직접 촬영해 수집하는 인터렉션을 제공합니다!  
  
**3. 🧩 부분 해설**  
- 작품의 긴 해설을 여러 부분으로 나누어 소제목별로 해설을 제공합니다. 다국어 해설, 오디오 해설, 배속 기능도 제공합니다!  
  
**4. 🤔 GPT로 궁금한 부분 해설 보기**  
- 한 작품에서 원하는 부분의 해설만 빠르게 볼 수 있습니다. 메인 화면에서 카메라를 켜 그림의 궁금한 부분을 찍으면 GPT를 통해 해당 부분의 해설을 바로 얻어볼 수 있습니다.  
<br>  

###  User Flow 
<br>  
<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/750880ba-e9a2-4b26-a080-09682b85cfd7" width="800">
<br>  

### 팀원  
| |<img src="https://avatars.githubusercontent.com/u/81066502?v=4" width="100"> | <img src="https://avatars.githubusercontent.com/u/98469609?v=4)" width="100"> | <img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/cb8d9f87-5d67-4ccc-91ea-e41ce007f6a4" width="100"> |  
|:--:|:----------:|:----------:|:----------:|  
| Member |공지나|유승연|최예원|  
| Role | Backend 개발 | Frontend 개발,<br> UI/UX 디자인 | Backend 개발|  
<br>  
  
## 주요 기능  
> - 🎨OCR을 통한 작품 검색  
> - 🔎 YOLO 작품 요소 인식  
> - 🧩부분 해설  
> - 🤔 GPT로 궁금한 부분 해설 보기  
  
|분류|기능1|기능2|  
|------|------|---|  
|**OCR로 작품 검색**|<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/ab9a7983-0cec-4bb4-a58e-4fd8a3d029bc" width="150"> |<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/3a708eac-fcf4-4653-a6a4-3f5c4dcecfa7" width="120"> |  
||• 작품 캡션 촬영을 모바일 카메라로 촬영 시 OCR로 작품을 인식 |• 작품명 외 다른 텍스트가 함께 인식되어도 Jaro Winkler 알고리즘으로 유사한 작품명 탐색해 제공  |  
|**YOLO로 작품 요소 인식** | <img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/806a71f0-ff0c-444b-8a9d-096d58d4171b" width="140"><br><img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/45c4da09-59e2-4831-b9f9-dd21d7993fc2" width="200">  |<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/575858c6-9679-4d26-9dea-15600333f7c7" width="120"> |  
| | • YOLOv8 모델로 국내 미술품 dataset 학습 <br> • 모바일 카메라로 요소 촬영 <br>  | • 요소 인식 성공 시 수집하기 버튼으로 도감에 등록 <br> • 도감에서 뱃지와 경험치, 수집한 작품 확인 |  
|**부분 해설**|<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/cad3333a-121f-4e9f-975b-91a7ed019ff7" width="110"> |<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/44bb4126-5d1b-431e-9c1e-9b5f0ee33082" width="110"> |  
|  | • 작품의 긴 해설을 여러 부분으로 나누어 소제목별로 해설 제공 <br> • 국내 주요 미술관 작품들의 정보 제공 |• 다국어 해설 지원: i18next로 언어 감지 후 한국어일 경우 캐시된 해설 표시. 그 외 언어의 경우 Translate API에 캐싱된 해설을 보내 번역된 해설 표시 <br> • 오디오 해설과 배속 기능: 해설 텍스트를 TTS API에 보내 음성 데이터를 받아와 Audio 객체로 오디오 재생 상태관리를 통해 일시정지, 배속 기능 제공 |  
|**GPT로 궁금한 부분 해설 보기**|<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/3dbc9192-310e-480d-b1f8-ce2223837237" width="100"><br> <img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/b7f207f7-1df2-428a-a34f-27bed1bcc8c3" width="120"> |<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/45cc477e-871c-484d-87da-b5803c772701" width="120"> |  
||• 홈 화면에서 카메라로 작품의 궁금한 부분을 촬영 <br> • YOLO로 인식한 후 어느 작품의 부분인지 판단 후 서버에 전송 | • 서버에서 DB에 작품이 있는지 검증한 후, 전체 해설 context를 GPT에 프롬포트로 전송해 최적의 파라미터로 커스터마이징한 gpt-3.5 모델에 해설 요청 |  
<br>
  
## ERD  

<img src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/81066502/4d5e4dfc-1fa2-4983-817d-4c04255e419a" width="900">
<br>

## API 
<img width="789" alt="스크린샷 2023-12-08 15 19 22" src="https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/2f8d2014-df86-404e-b139-aa050b7d75d5">
<br>

## 파일 디렉토리 
 
### FrontEnd Repo.

```
└───📂src
    ├───📂api
    ├───📂assets
    │   ├───📂icon
    │   │               ├───📁bottomBar
    │   │               ├───📁common
    │   │               ├───📁detection
    │   │               ├───📁login
    │   │               ├───📁mainPage
    │   │               └───📁search
    │   ├───📂image
    │   │               └───📁badge
    │   └───📂logo
    ├───📂components
    │   ├───📂Collection
    │   ├───📂Common
    │   ├───📂Detection
    │   │               └───📁Button
    │   ├───📂Layout
    │   ├───📂MainPage
    │   ├───📂Search
    │   ├───📂Docent
    │   ├───📂Mypage
    ├───📂pages
    │   ├───📂docent
    │   └───📂setting
    └───📂style
```
<br>

### BackEnd Repo.
```
└───📂src
    ├───📂main
    │   ├───📂generated
    │   ├───📂java.com.example.eight
    │   │               ├───📁artwork
    │   │                   ├───📁controller
    │   │                   ├───📁dto
    │   │                   ├───📁entity
    │   │                   ├───📁repository
    │   │                   ├───📁service
    │   │               ├───📁collection
    │   │                   ├───📁controller
    │   │                   ├───📁dto
    │   │                   ├───📁service
    │   │               ├───📁global
    │   │                   ├───📁config
    │   │                   ├───📁jwt
    │   │                   │   └───📁controller
    │   │                   │   └───📁dto
    │   │                   │   └───📁service
    │   │                   ├───📁oauth2
    │   │                   │   └───📁controller
    │   │                   │   └───📁dto
    │   │                   │   └───📁service
    │   │               ├───📁gpt
    │   │                   ├───📁controller
    │   │                   ├───📁dto
    │   │                   ├───📁service    
    │   │               ├───📁user
    │   │                   ├───📁controller
    │   │                   ├───📁dto
    │   │                   ├───📁entity
    │   │                   ├───📁repository
    │   │                   └───📁service
    │   └───📂resources
    │  
    └───📂test
```
<br>

## 기술스택
### 📱 Built With
FE : <img src="https://img.shields.io/badge/react-61DAFB?style=flat&logo=react&logoColor=white"/> <img src="https://img.shields.io/badge/flutter-02569B?style=flat&logo=flutter&logoColor=white"/> <img src="https://img.shields.io/badge/javascript-FFE141?style=flat&logo=javascript&logoColor=black"/>

BE : &nbsp; <img src="https://img.shields.io/badge/Java-A8B9CC?style=flat&logo=openjdk&logoColor=white"/> <img src="https://img.shields.io/badge/Spring-5cb230?style=flat&logo=spring&logoColor=white"/> <img src="https://img.shields.io/badge/REDIS-DC382D?style=flat&logo=REDIS&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/> <img src="https://img.shields.io/badge/AWS EC2-FF9900?style=flat&logo=amazon EC2&logoColor=white"/>  <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=flat&logo=amazon RDS&logoColor=white"/>  <img src="https://img.shields.io/badge/AWS ElastiCache-005571?style=flat&logo=Amazon&logoColor=white"/> <img src="https://img.shields.io/badge/AWS Elastic LoadBalancer-A100FF?style=flat&logo=amazon&logoColor=white"/>  
<img src="https://img.shields.io/badge/JWT-000000?style=flat&logo=JSON Web Tokens&logoColor=white"/>  <img src="https://img.shields.io/badge/Oauth2.0-4285F4?style=flat&logo=Google&logoColor=white"/>  


CI/CD: <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=flat&logo=GitHub Actions&logoColor=white"/>  <img src="https://img.shields.io/badge/AWS S3-569A31?style=flat&logo=Amazon S3&logoColor=white"/> <img src="https://img.shields.io/badge/AWS CodeDeploy-232F3E?style=flat&logo=amazon&logoColor=white"/>

Infra Management: <img src="https://img.shields.io/badge/AWS Lambda-FF9900?style=flat&logo=AWS Lambda&logoColor=white"/> <img src="https://img.shields.io/badge/AWS SNS-BC52EE?style=flat&logo=Amazon&logoColor=white"/> <img src="https://img.shields.io/badge/AWS API Gateway-FF4F8B?style=flat&logo=Amazon API Gateway&logoColor=white"/>

<br>

### 시스템 아키텍처  
![Group 632600](https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/assets/77966605/d8dd87c2-13fb-48e4-8e10-00ee619a886a)
<br>  
 
## 시작 가이드 

> Requirements: Java 17, Spring 3.1.4
> 
> ArtHunter 환경 변수 설정을 완료해야 프로젝트가 실행됩니다.
> 환경 변수 세팅이 되어있지 않으면 **How to test** 섹션으로 가주세요.

### **How to build  & install**  

**1. 프로젝트 클론**
```
git clone https://github.com/CAPSTON-EIGHT/EIGHT_SERVER.git
cd EIGHT_SERVER
```
**2. .env 파일 추가**
프로젝트 루트 디렉토리에 `.env` 파일을 생성하고 아래와 같이 설정해주세요. 
```
# .env 파일 구조는 key-value 형식으로 입력해주세요. 
# 예시 
OPENAI_API_KEY=Open111Ai085&key 
```

**3. 실행**
```
./gradlew build cd build/libs 

# .env 파일 로드  
source ../.env 

# Jar 파일 실행 
java -jar eight-0.0.1-SNAPSHOT.jar
```
### **How to test**  

 - 배포된 **[Art Hunter 사이트](https://art-hunter.site/login)** 에 접속합니다. 
- **Google 계정 로그인**만 진행하면 모든 서비스가 이용 가능합니다.

<br>
  
  
## 오픈 소스 

 - 공공데이터: 문화체육관광부 국립중앙박물관_전국 박물관 유물정보 
   - 전국 주요 박물관의 소장품 관련 정보를 데이터로 제공받았습니다. 
 - 해설 데이터: 국립중앙박물관
   - 소장품 해설을 데이터로 제공받았습니다. 

<br>

## Project Management

 - GitHub Project
   - ⭐ [FrontEnd](https://github.com/orgs/CAPSTON-EIGHT/projects/1)
    - 🌙 [BackEnd](https://github.com/orgs/CAPSTON-EIGHT/projects/2)
 - [GitHub Issues](https://github.com/CAPSTON-EIGHT/EIGHT_SERVER/pulls)
<br>

## Project Documents & Links
- [사용 설명서](https://drive.google.com/file/d/14BW2b7J2yuRQ-A0smFlo1fbY6TAj8z3l/view?usp=sharing)
- [포스터](https://drive.google.com/file/d/1xh6_cICeedILEmieWpait5j-u0iqe_3R/view?usp=sharing)
- [데모 동영상](https://drive.google.com/file/d/1hzRkkm8EocHLPcG_BoNNqaMAxY9mZCDT/view?usp=drive_link)
