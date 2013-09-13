Readme  Ver. 2013. 09.04
Project ssuherb
권한: 팀 herbitssu
작성: 
 13.08.17 팀 herbitssu 멤버 권태헌
 13.08.27 팀 herbitssu 멤버 김준호
 
 #버전 패치 노트는 필요 라이브러리 아래에서 최신순으로 나열
 
 수정:
 13.08.29 		권태헌
 13.08.30		김준호
 13.09.04		김준호
 13.09.04		김준호		Ver 1.0.0
 13.09.07		진민규		Ver 1.1.0
 13.09.07		김준호		Ver 1.1.1
 13.09.13		진민규		Ver 1.1.2


 필요 라이브러리:
jython.jar
mysql-connector-java-commercial-5.1.25-bin.jar
jfreechart-1.0.15
tween-engine-api.jar
tween-engine-api-sources.jar
Jama-1.0.3
commons-math-2.2

############################# 패치 노트 ####################################

13.09.13	Ver 1.1.2

1. StorePanel에서 DB를 기준으로 Graph를 그려 Visualizing 해주는 기능 추가
	=> 이와 관련해 StoreGraphPanel과 DateUtil Class가 추가됨
	StoreGraphPanel은 paint 메소드의 Graphic Instance를 이용하여 그래프를 그림
	DateUtil은 String/Long/Usertype/Calendar 등의 다양한 날짜-시간 정보를 상호 변환

------------------------------------------------------------------------

13.09.07	Ver 1.1.1

1. PythonSyncModule에 db herb_menu 테이블에서 메뉴 정보를 얻어오는 정보 추가
	- MenuList 클래스를 스태틱으로 만들고(생성자가 유용하지 않도록), Dictionary 타입의 변수
		herbMenu 생성 (key = Integer(menu_id) , value = String(menu_name)


----------------------------------------------------------------------------

13.09.07	Ver 1.1.0

OrderSystem.java
1. OrderSystem 생성시 DB 접속 시점을, DB Generator 객체 생성 직후로 변경
 => 기존에는 Store Panel 이 생성된 후에야 DB 접속이 이뤄져, Store Panel에서 DB를 사용할 수 없었음
 => 이 때문에 DB 접속에 걸리는 시간으로, 프로그램 로딩이 느려짐. Splash 화면을 띄워주는 등의 대책 필요
 
DB
2. herb_inventory, herb_invenlog 테이블 생성 및 Test Data Injection 완료
 herb_inventory 테이블 생성
 os.db.exec("create table herb_inventory("
  + " inventory_id int(11),"
  + " inventory_name varchar(45),"
  + " inventory_unit varchar(10),"
  + " inventory_regdate datetime"
  + ");"
 );
 
 herb_invenlog 테이블 생성
 os.db.exec("create table herb_invenlog("
  + " invenlog_id int(11),"
  + " invenlog_inventory_id int(11),"
  + " invenlog_value int(11),"
  + " invenlog_regdate datetime"
  + ");"
 );
 
 herb_invenlog에 Test Data Injection
 os.db.exec("DELETE FROM herb_invenlog;"); 
 // Test Data Injection
 String sql;
 for (int i = 1; i <= 140; i++) {
  sql = "insert herb_invenlog("
    + "invenlog_id, invenlog_inventory_id, invenlog_value, invenlog_regdate) "
    + "values("
    + "'" + Integer.toString(i) + "', " //inventory_id
    + "'" + Integer.toString(((i-1) % 20) + 1) + "', " //inventory_name
    + "'" + Integer.toString(new Random().nextInt(40)) + "', "
    + "date_add(now(), interval -" + Integer.toString(7-(i-1)/20) + " day))";
  os.db.exec( //inventory_regdate
    sql
  );
 }
 
StorePanel.java
3. 재고관리(Store Panel)
 1) herb_inventory 테이블에서, 재고관리할 목록을 읽어들여 동적으로 버튼을 생성한다.
 2) 재고관리할 버튼을 누르면 콘솔에 해당 재고 기록이 뜬다
 (오른쪽 패널에서 그래프와 함께 관리 내용이 뜬다.)

----------------------------------------------------------------------------
13.09.04	Ver 1.0.0

1. JythonDriver 생성시 넘겨주는 인자 값으로 구분하여 파이썬 모듈 실행
	- Building : 기존 Building 모듈,
	- Apriori : 파이썬 모듈로 구현한 예측 알고리즘 (현재 예제 데이터로 예측)
	
2. PythonSyncModule 구현
	- 실행시 DB에 쿼리 질의(현재는 herb_order의 모든 내용을 긁어옴, 추후 수정 예정)
	- 자이썬 드라이버 객체 생성을 통해 예측 알고리즘 시행
	- 추후 JProgressBar를 통해 진행 사항을 눈에 보이도록 설정 예정
	- 현재 쿼리 질의 결과 및 알고리즘 수행 결과는 새로운 프레임의 JTextArea에 넣음
	
3. JDBC 주소 수정
	- 테스트를 위해 임의로 수정(Ubuntu Server 12.04 : 192.168.32.148이 주소. 본인 구현시
		본인의 상황에 맞게 수정하여 개발 요청)
		
----------------------------------------------------------------------------


####################### JAVA #########################

08.30 - 필요없는 패키지 정리 ( SlideLayout Demo Package)
		결제 버튼 누른 후 다른 패널로 이동시 결제창이 그대로 남아 있던것 수정
		
09.04 - Apiroir Python 모듈 적용을 위한 HerbAprioriType 인터페이스 추가
		메소드 호출 : public void getResult()
		JythonDriver에 Jama Test 대신 HerbApriori.py 테스트 코드로 교체 
		Table button 클릭 할 때 apriori 예제 코드 실행(stdout)

####################### JAVA END #####################

**********************////////Jython/////////////////*****************
##### 필요: jython.jar

#### 패키지 interfaces, JythonObjectFactory 
#### 파이썬 Building.py 클래스
//현재 JythonDriver클래스에서 JythonObjectFactory 클래스를 이용하여 
//자이썬 클래스 Building을 생성하고 Building 내의 메소드를 호출한다.
//numpy를 활용한 BuildingType (자바 인터페이스)와
//Building (파이썬 클래스)구현 필요

08.27 추가 사항
1. JythonObjectFactory.java에 파이썬 사용자 라이브러리 추가
2. Building.py에 numpy(Custom Library) import ### NOT TESTED ###


08.29 수정 사항
1. numpy 사용 불가함에 따라 Jama( Java Matrix), Common-Math 라이브러리 추가
2. Building.py 내 생성자에 테스트해둔 행렬 곱의 소스내용을 참조하여 파워메소드(판매예측),
연관관계 분석(판매권유) 시스템 제작하는 방향으로.

09.04 수정 사항
1. HerbApriori.py 모듈 추가, Python Standard 라이브러리 활용하여 알고리즘 수행하도록 수정
2. 남은 진행 : DB -> Java -> Parsing -> Python -> Java
3. 계획 : PythonSync 등을 통해 알고리즘 수행 방향(알고리즘이 수행 할 때 시간이 상당하게 소요됨(3~4초)

***********************/////Jython 끝//////////////****************************




************************/////////////// DB ///////////////// ****************************
DB의 내용은 한글파일 참조
****************************///DB 끝////********************************************