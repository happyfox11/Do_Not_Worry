# Don't Worry!

Don't worry about obsessive-compulsive disorder!  
- 주제 : 확인강박 치료 앱 

- 확인강박 :  
무언가를 했거나 안했는지를 자신이 확실히 했거나 안했는지 인지 했음에도 불구하고 지속하여 혹시나 하는 마음에 확인하는 강박증  


- 메뉴  
1. 안심 서비스  
  1.1 이전 정보 조회  
    : 과거의 체크리스트의 정보를 확인하면서, 이전에 내가 염려했던 일이 일어나지 않았음을 확인하는 것으로 치료에 도움이 될 수 있다.
  1.2 레벨 선택하기  
    : 직접 관리하고 싶은 항목을 추가 및 삭제하여 체크리스트를 생성하고, 레벨을 선택하여 외출전 체크리스트를 통해 확인을 완료한다.  
    1.2.1 Lv1  
      : 확인 리스트 제공, 한 개의 최종 확인 버튼 제공  
    1.2.2 Lv2  
      : 각 항목 당 세부 체크 리스트 제공, 최종 확인 버튼 제공  
    1.2.3 Lv3  
      : 사진을 이용한 확실한 방법 제공, 최종 확인 버튼 제공  
  1.3 레벨 추천받기  
    : 몇 가지 질문으로 Lv1(심하지 않은 단계), Lv2(중간 단계), Lv3(심한 강박단계)를 파악하여 자신의 상태에 맞는 레벨을 추천해준다. 
2. 도움 정보  
  : 확인강박을 치료하기 위해 쉽게 따라할 수 있는 몇 가지 방법들을 제공한다.
3. 도움 기관
  : 도움을 받을 수 있는 기관의 정보를 제공한다.
  
  
  
1. 메인 화면 : Music Service  (permisson : FOREGROUND_SERVICE, MediaPlayer, Service, BroadcastReceiver, Disposable (Thread 역할))
2. Timer Fragment : Notification(알림)  
3. 도움 기관 : Naver Search API(permission:Internet), Custom ListView  
4. 회원가입 및 로그인 : Firebase Authentication (Email & Password)  
5. 도움 정보 : ViewPager & Fragment  
6. 체크리스트 항목 관리 : ListView(CHOICE_MODE_MULTIPLE)  
7. Level 1, 2, 3 : TabLayout & ViewPager, SQLite(Insert)  
8. Level 3 방식 : Camera(permission:WRITE_EXTERNAL_STORAGE, FileProvider)  
9. 이전정보조회 : CalendarView(OnDateChangeListener), SQLite(Select)  

