Readme  Ver. 2013. 09.04
Project ssuherb
����: �� herbitssu
�ۼ�: 
 13.08.17 �� herbitssu ��� ������
 13.08.27 �� herbitssu ��� ����ȣ
 
 #���� ��ġ ��Ʈ�� �ʿ� ���̺귯�� �Ʒ����� �ֽż����� ����
 
 ����:
 13.08.29 		������
 13.08.30		����ȣ
 13.09.04		����ȣ
 13.09.04		����ȣ		Ver 1.0.0
 13.09.07		���α�		Ver 1.1.0
 13.09.07		����ȣ		Ver 1.1.1
 13.09.13		���α�		Ver 1.1.2


 �ʿ� ���̺귯��:
jython.jar
mysql-connector-java-commercial-5.1.25-bin.jar
jfreechart-1.0.15
tween-engine-api.jar
tween-engine-api-sources.jar
Jama-1.0.3
commons-math-2.2

############################# ��ġ ��Ʈ ####################################

13.09.13	Ver 1.1.2

1. StorePanel���� DB�� �������� Graph�� �׷� Visualizing ���ִ� ��� �߰�
	=> �̿� ������ StoreGraphPanel�� DateUtil Class�� �߰���
	StoreGraphPanel�� paint �޼ҵ��� Graphic Instance�� �̿��Ͽ� �׷����� �׸�
	DateUtil�� String/Long/Usertype/Calendar ���� �پ��� ��¥-�ð� ������ ��ȣ ��ȯ

------------------------------------------------------------------------

13.09.07	Ver 1.1.1

1. PythonSyncModule�� db herb_menu ���̺��� �޴� ������ ������ ���� �߰�
	- MenuList Ŭ������ ����ƽ���� �����(�����ڰ� �������� �ʵ���), Dictionary Ÿ���� ����
		herbMenu ���� (key = Integer(menu_id) , value = String(menu_name)


----------------------------------------------------------------------------

13.09.07	Ver 1.1.0

OrderSystem.java
1. OrderSystem ������ DB ���� ������, DB Generator ��ü ���� ���ķ� ����
 => �������� Store Panel �� ������ �Ŀ��� DB ������ �̷���, Store Panel���� DB�� ����� �� ������
 => �� ������ DB ���ӿ� �ɸ��� �ð�����, ���α׷� �ε��� ������. Splash ȭ���� ����ִ� ���� ��å �ʿ�
 
DB
2. herb_inventory, herb_invenlog ���̺� ���� �� Test Data Injection �Ϸ�
 herb_inventory ���̺� ����
 os.db.exec("create table herb_inventory("
  + " inventory_id int(11),"
  + " inventory_name varchar(45),"
  + " inventory_unit varchar(10),"
  + " inventory_regdate datetime"
  + ");"
 );
 
 herb_invenlog ���̺� ����
 os.db.exec("create table herb_invenlog("
  + " invenlog_id int(11),"
  + " invenlog_inventory_id int(11),"
  + " invenlog_value int(11),"
  + " invenlog_regdate datetime"
  + ");"
 );
 
 herb_invenlog�� Test Data Injection
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
3. ������(Store Panel)
 1) herb_inventory ���̺���, �������� ����� �о�鿩 �������� ��ư�� �����Ѵ�.
 2) �������� ��ư�� ������ �ֿܼ� �ش� ��� ����� ���
 (������ �гο��� �׷����� �Բ� ���� ������ ���.)

----------------------------------------------------------------------------
13.09.04	Ver 1.0.0

1. JythonDriver ������ �Ѱ��ִ� ���� ������ �����Ͽ� ���̽� ��� ����
	- Building : ���� Building ���,
	- Apriori : ���̽� ���� ������ ���� �˰��� (���� ���� �����ͷ� ����)
	
2. PythonSyncModule ����
	- ����� DB�� ���� ����(����� herb_order�� ��� ������ �ܾ��, ���� ���� ����)
	- ���̽� ����̹� ��ü ������ ���� ���� �˰��� ����
	- ���� JProgressBar�� ���� ���� ������ ���� ���̵��� ���� ����
	- ���� ���� ���� ��� �� �˰��� ���� ����� ���ο� �������� JTextArea�� ����
	
3. JDBC �ּ� ����
	- �׽�Ʈ�� ���� ���Ƿ� ����(Ubuntu Server 12.04 : 192.168.32.148�� �ּ�. ���� ������
		������ ��Ȳ�� �°� �����Ͽ� ���� ��û)
		
----------------------------------------------------------------------------


####################### JAVA #########################

08.30 - �ʿ���� ��Ű�� ���� ( SlideLayout Demo Package)
		���� ��ư ���� �� �ٸ� �гη� �̵��� ����â�� �״�� ���� �ִ��� ����
		
09.04 - Apiroir Python ��� ������ ���� HerbAprioriType �������̽� �߰�
		�޼ҵ� ȣ�� : public void getResult()
		JythonDriver�� Jama Test ��� HerbApriori.py �׽�Ʈ �ڵ�� ��ü 
		Table button Ŭ�� �� �� apriori ���� �ڵ� ����(stdout)

####################### JAVA END #####################

**********************////////Jython/////////////////*****************
##### �ʿ�: jython.jar

#### ��Ű�� interfaces, JythonObjectFactory 
#### ���̽� Building.py Ŭ����
//���� JythonDriverŬ�������� JythonObjectFactory Ŭ������ �̿��Ͽ� 
//���̽� Ŭ���� Building�� �����ϰ� Building ���� �޼ҵ带 ȣ���Ѵ�.
//numpy�� Ȱ���� BuildingType (�ڹ� �������̽�)��
//Building (���̽� Ŭ����)���� �ʿ�

08.27 �߰� ����
1. JythonObjectFactory.java�� ���̽� ����� ���̺귯�� �߰�
2. Building.py�� numpy(Custom Library) import ### NOT TESTED ###


08.29 ���� ����
1. numpy ��� �Ұ��Կ� ���� Jama( Java Matrix), Common-Math ���̺귯�� �߰�
2. Building.py �� �����ڿ� �׽�Ʈ�ص� ��� ���� �ҽ������� �����Ͽ� �Ŀ��޼ҵ�(�Ǹſ���),
�������� �м�(�Ǹű���) �ý��� �����ϴ� ��������.

09.04 ���� ����
1. HerbApriori.py ��� �߰�, Python Standard ���̺귯�� Ȱ���Ͽ� �˰��� �����ϵ��� ����
2. ���� ���� : DB -> Java -> Parsing -> Python -> Java
3. ��ȹ : PythonSync ���� ���� �˰��� ���� ����(�˰����� ���� �� �� �ð��� ����ϰ� �ҿ��(3~4��)

***********************/////Jython ��//////////////****************************




************************/////////////// DB ///////////////// ****************************
DB�� ������ �ѱ����� ����
****************************///DB ��////********************************************