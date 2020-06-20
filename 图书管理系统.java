import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;



public class Sql_java1 {
public static void main(String args[]) throws Exception {
int choose;
choose=menu();
while(choose!=3) {
switch(choose) {
case 1:insert();break;
case 2:select();break;
case 3:System.out.println("程序退出！");return;
default:System.out.println("输入无效！请重新输入");break;
}
choose=menu();
}

}
public static void insert() throws Exception{
Class.forName("com.mysql.jdbc.Driver");
Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mis?useUnicode=true&characterEncoding=utf-8","root","liuxin520");
String sql1="insert into tstudent(id,name,gender,birth,score) values(?,?,?,?,?)";

PreparedStatement ps1=con.prepareStatement(sql1);
String s1="";
System.out.println("请输入图书信息，ID，name，gender，score,以逗号分隔,end结束");
while(s1.equals("end")!=true) {
Scanner scan=new Scanner(System.in);
s1=scan.nextLine();
String[] strs=s1.split(",|，");
if(strs.length==5) {
ps1.setString(1,strs[0]);
ps1.setString(2,strs[1]);
ps1.setString(3, strs[2]);
ps1.setString(4, strs[3]);
ps1.setFloat(5,Float.parseFloat( strs[4]));

}
}

int counts=ps1.executeUpdate();
System.out.println(counts);
System.out.println("插入图书信息成功！");
ps1.close();
con.close();
}
public static void select() throws Exception{
//?useUnicode=true&characterEncoding=utf-8
Class.forName("com.mysql.jdbc.Driver");
Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mis?useUnicode=true&characterEncoding=utf-8","root","liuxin520");
String sql2="select * from tstudent where id=?";
PreparedStatement ps2=con.prepareStatement(sql2);
System.out.println("请输入图书号码:");
Scanner scan=new Scanner(System.in);
String s=scan.nextLine();
ps2.setString(1, s);
ResultSet rs=ps2.executeQuery();
//ps2.setString(1, s);
while(rs.next()) {
String id=rs.getString("id");
String name=rs.getString("name");
String gender=rs.getString("gender");
System.out.println(id+"\t"+name+"\t"+gender+"\t"+birth+"\t"+score);
}
ps2.close();
rs.close();
con.close();
}

public static int menu() {
int choose;
System.out.println("图书管理系统");
System.out.println("1.插入图书信息");
System.out.println("2.查询图书信息");
System.out.println("3.退出");
Scanner scan=new Scanner(System.in);
System.out.println("请输入您的选择:");
choose=scan.nextInt();
return choose;
}

}






import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class Sql_java2 {
public static void main(String args[]) throws Exception {
/*
Properties pro1=new Properties();
pro1.setProperty("lsh","0");
FileOutputStream fos1=new FileOutputStream("./lsh.property");
String description1="";
pro1.store(new OutputStreamWriter(fos1,"utf-8"), description1);
fos1.close();
*/
int lsh=0;
Class.forName("com.mysql.jdbc.Driver");
Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sale?useUnicode=true&characterEncoding=utf-8","root","liuxin520");
String sql1="select * from tuser";
PreparedStatement ps1=con.prepareStatement(sql1);
//ResultSet rs=ps1.executeQuery();
System.out.println("欢迎使用图书管理系统，请登陆");
int i=0;
int t=0;
String user="";
String chrName="";
String role="";
String password="";
while(i<=2) {
ResultSet rs=ps1.executeQuery();
Scanner scan=new Scanner(System.in);
System.out.println("请输入用户名");
String userName= scan.nextLine();
System.out.println("请输入密码");
String password1=scan.nextLine();
while(rs.next()) {
String user1=rs.getString("userName");
String pw1=rs.getString("password");
if(user1.equalsIgnoreCase(userName) && pw1.equalsIgnoreCase(password1)) {
t=1;
user=user1;
password=password1;
chrName=rs.getString("chrName");
role=rs.getString("role");
break;
}
}
if(t==0) {
System.out.println("用户名或密码不正确，请重新输入!");
}
if(t==1) {

break;
}
i++;
rs.close();
}
if(i>=3) {
System.out.println("最多只能尝试3次,程序退出!");
return;
}
int choose=menu(chrName);
while(choose!=6) {
switch(choose) {
case 1:charge(con,user);break;
case 2:inquirystatistics(con,user);break;//查询统计
case 3:Goodsmaintenance( con,role);break;
case 4:changepassword(con,password,user);break;//修改密码
case 5:dataoutput(con,password,user);break;//数据导出
case 6:return;//退出
default:System.out.println("输入无效，只能输入1-6，请重新输入");break;
}
choose=menu(chrName);
}
con.close();

}
public static int ReadFile() throws Exception{
Properties pro3=new Properties();
InputStream in3= new BufferedInputStream(new FileInputStream("./lsh.property"));
//pro3.load(new InputStreamReader(in3),"utf-8");
pro3.load(new InputStreamReader(in3));
in3.close();
String lsh=pro3.getProperty("lsh");
int x=Integer.parseInt(lsh);
//System.out.println(userName+password);
return x;
}
public static void ChangeFile() throws Exception{
Properties pro3=new Properties();
InputStream in3= new BufferedInputStream(new FileInputStream("./lsh.property"));
pro3.load(new InputStreamReader(in3));
in3.close();
int lsh=ReadFile();
pro3.setProperty("lsh", String.valueOf(lsh+1));
String description2="";
FileOutputStream fos=new FileOutputStream("./lsh.property");
pro3.store(new OutputStreamWriter(fos,"utf-8"),description2 );
fos.close();
}
//收费
public static void charge(Connection con,String user) throws Exception {
int lsh=ReadFile();
ChangeFile();
lsh++;
//System.out.println(lsh);
int count=0;
String sql1="select * from tproduct";
String sql2="insert into  tsaledetail(lsh,barCode,productName,price,count,operator,saleTime) values(?,?,?,?,?,?,?)";
Pattern p1=Pattern.compile("[0-9]{6}");
int t=0;
String barCode="";
String productName="";
float price=0;
String supply;
System.out.println("请输入条形码(6位数字字符)");
Scanner scan=new Scanner(System.in);
String s=scan.nextLine();
Matcher m1=p1.matcher(s);
boolean haspic=m1.find();
while(haspic==false || t==0) {
Scanner scan1=new Scanner(System.in);
if(haspic==false) {
System.out.println("条形码输入格式不正确，请重新输入:");
s=scan.nextLine();
m1=p1.matcher(s);
haspic=m1.find();
}
if(haspic==true) {
PreparedStatement ps2=con.prepareStatement(sql1);
ResultSet rs=ps2.executeQuery();
while(rs.next()) {
if(rs.getString("barCode").equals(s)) {
barCode=rs.getString("barCode");
productName=rs.getString("productName");
price=rs.getFloat("price");
supply=rs.getString("supply");
t=1;
break;
}
}
if(t==0) {
System.out.println("您输入的条形码不存在，请确认后重新输入");
s=scan.nextLine();
m1=p1.matcher(s);
haspic=m1.find();
}
if(t==1) {
break;
}
ps2.close();
rs.close();
}
}
//流水号
String Lsh="";
for(int i=0;i<5-String.valueOf(lsh).length();i++) {
Lsh=Lsh+"0";
}
Lsh=Lsh+String.valueOf(lsh);
if(t==1) {
System.out.println("请输入图书数量:");
count=scan.nextInt();
}
//System.out.println(Lsh);
//获取当前系统时间
Date date=new Date();
SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String datetime=format.format(date);
//插入一条记录
//System.out.println(Lsh+barCode+productName+price+count+user+datetime);
PreparedStatement ps2=con.prepareStatement(sql2);
ps2.setString(1, Lsh);
ps2.setString(2, barCode);
ps2.setString(3, productName);
ps2.setFloat(4, price);
ps2.setInt(5, count);
ps2.setString(6, user);
ps2.setString(7, datetime);
int counts=ps2.executeUpdate();
System.out.println("成功增加"+counts+"笔借书数据");
ps2.close();
}
//查询统计inquirystatistics
public static void inquirystatistics(Connection con,String user) throws Exception{
System.out.println("请输入日期(yyyy-MM-dd):");
Pattern p1=Pattern.compile("([0-9]{4})[-]([0-9]{2})[-]([0-9]{2})");
String s="";
boolean haspic=false; 
while(haspic==false) {
Scanner scan=new Scanner(System.in);
s=scan.nextLine();
Matcher m1=p1.matcher(s);
haspic=m1.find();
if(haspic==true) {
break;
}
else {
System.out.println("你输入的日期格式不正确，请重新输入：");
}
}
int salecount=0;
int productcount=0;
float allmoney=0;
String sql="select * from tsaledetail";
PreparedStatement ps1=con.prepareStatement(sql);
ResultSet rs=ps1.executeQuery();
ArrayList<String> list=new ArrayList<String>();
while(rs.next()) {
String datad=rs.getString("saleTime");
String[] datetime=datad.split(" ");
if(datetime[0].equals(s)) {
String lsh=rs.getString("lsh");
String  productName=rs.getString("productName");
float price=rs.getFloat("price");
int count=rs.getInt("count");
float money=price*count;
String time=datetime[1];
String list1=lsh+"\t"+productName+"\t"+price+"\t"+count+"\t"+money+"\t"+time+"\t"+user;
list.add(list1);
salecount++;
productcount=productcount+count;
allmoney=allmoney+money;
}
}
String[] slist=s.split("-");
System.out.println(slist[0]+"年"+slist[1]+"月"+slist[2]+"如下");

System.out.println("===\t===\t===\t===\t===\t===\t\t==");
for(String ss:list) {
System.out.println(ss);
}

System.out.println("日期："+slist[0]+"年"+slist[1]+"月"+slist[2]+"日");
System.out.println("请按任意键返回主界面");
ps1.close();
rs.close();
System.in.read();
}
//商品维护Goods maintenance
public static void Goodsmaintenance(Connection con,String role) throws Exception{
if(role.equals("管理员")) {
System.out.println("当前用户没有执行该项功能的权限");
return;
}
if(role.equals("管理员")) {
int choose=menu2();
while(choose!=5) {
switch(choose) {
case 1:ReadExcel(con);break;//从excel中导入数据
case 2:ReadTXT(con);break;//从文本文件导入数据
case 3:ReadfromKeyboard(con);break;//键盘输入
case 4:inquiry(con);break;//按商品名称查询
case 5:return;//返回主菜单
default:System.out.println("输入无效，只能输入1-5，请重新输入");break;
}
choose=menu2();
}
}
}
//从excel中导入数据
public static void ReadExcel(Connection con) throws Exception{
ArrayList<String> slist=new ArrayList<String>();
int count=0;
String sql1="insert into tproduct(barCode,productName,price,supply) values(?,?,?,?)";
String sql2="select * from tproduct";
//PreparedStatement ps1=con.prepareStatement(sql1);
//PreparedStatement ps2=con.prepareStatement(sql2);
Workbook workbook = Workbook.getWorkbook(new File("./product.xls"));
Sheet sheet = workbook.getSheet(0);
for(int i=1;i<sheet.getRows();i++) {
for(int j=0;j<sheet.getColumns();j++) {
Cell cell=sheet.getCell(j, i);
slist.add(cell.getContents());
System.out.print(cell.getContents());
}
int m=0;
PreparedStatement ps2=con.prepareStatement(sql2);
ResultSet rs=ps2.executeQuery();
while(rs.next()) {
if(rs.getString("barCode").equals(slist.get(0))) {
m=1;
}
}
if(m==0) {
PreparedStatement ps1=con.prepareStatement(sql1);
ps1.setString(1, slist.get(0));
ps1.setString(2, slist.get(1));
ps1.setFloat(3, Float.parseFloat(slist.get(2)));
ps1.setString(4, slist.get(3));
count++;
int counts=ps1.executeUpdate();
ps1.close();
}
slist.clear();
rs.close();

}
System.out.println("成功从excel文件导入"+count+"条数据");
}
//从文本文件中导入数据
public static void ReadTXT(Connection con) throws Exception{
String sql1="insert into tproduct(barCode,productName,price,supply) values(?,?,?,?)";
String sql2="select * from tproduct";
int count=0;
ArrayList<String> strlist=new ArrayList<String>();
File fis = new File("./product.txt");
BufferedReader br=new BufferedReader(new FileReader(fis));
br.readLine();
String str="";
while((str=br.readLine())!=null) {
strlist.add(str);
}
for(String s:strlist) {
int m=0;
String[] slist=s.split(" ");
PreparedStatement ps2=con.prepareStatement(sql2);
ResultSet rs=ps2.executeQuery();
while(rs.next()) {
if(rs.getString("barCode").equals(slist[0])) {
m=1;
}
}
if(m==0) {
PreparedStatement ps1=con.prepareStatement(sql1);
ps1.setString(1,slist[0]);
ps1.setString(2, slist[1]);
ps1.setFloat(3, Float.parseFloat(slist[2]));
ps1.setString(4, slist[3]);
count++;
int counts=ps1.executeUpdate();
ps1.close();
}
rs.close();
}
System.out.println("成功从TXT文件导入"+count+"条数据");
}
//从键盘中导入数据
public static void ReadfromKeyboard(Connection con) throws Exception{
System.out.println("请输入条形码(6位数字字符)，名称，以,分隔)");
String sql1="insert into tproduct(barCode,productName,price,supply) values(?,?,?,?)";
String sql2="select * from tproduct";
int count=0;
String data="";
Scanner scan=new Scanner(System.in);
data=scan.nextLine();
int k=0;
String[] slist=data.split(",|，");
PreparedStatement ps2=con.prepareStatement(sql2);
ResultSet rs=ps2.executeQuery();
while(rs.next()) {
if(rs.getString("barCode").equals(slist[0])) {
System.out.println(rs.getString("barCode")+"条形码重复");
k=1;
}
}
if(k==0) {
PreparedStatement ps1=con.prepareStatement(sql1);
ps1.setString(1,slist[0]);
ps1.setString(2, slist[1]);
ps1.setFloat(3, Float.parseFloat(slist[2]));
ps1.setString(4, slist[3]);
count++;
int counts=ps1.executeUpdate();
ps1.close();
System.out.println("增加成功");
}
rs.close();
}

public static void inquiry(Connection con) throws Exception{
String sql2="select * from tproduct";
ArrayList<String> slist=new ArrayList<String>();
int i=0;
System.out.println("请输入(经费不够，不支持模糊查询):");
Scanner scan=new Scanner(System.in);
String goods=scan.nextLine();
PreparedStatement ps1=con.prepareStatement(sql2);
ResultSet rs=ps1.executeQuery();
while(rs.next()) {
if(rs.getString("productName").equals(goods)) {
i++;
String s=i+"\t"+rs.getString("barCode")+"\t"+goods+"\t"+rs.getString("price")+"\t"+rs.getString("supply");
slist.add(s);
}
}
System.out.println("满足条件的记录总共"+i+"条，信息如下：");

System.out.println("===\t=====\t=====\t==\t====");
for(String s1:slist) {
System.out.println(s1);
}
}
//修改密码
public static void changepassword(Connection con,String password,String user) throws Exception{
Pattern p1=Pattern.compile(".*[a-z].*");
Pattern p2=Pattern.compile(".*[A-Z].*");
Pattern p3=Pattern.compile(".*[0-9].*");
int t=0;
String s;
String s1="";
while(true) {
System.out.println("请输入原密码");
Scanner scan=new Scanner(System.in);
s=scan.nextLine();
//int t=0;
if(s.equals(password)) {
while(true) {
System.out.println("请输入新的密码:");
s1=scan.nextLine();
Matcher m1=p1.matcher(s1);
Matcher m2=p2.matcher(s1);
Matcher m3=p3.matcher(s1);
boolean haspic1=m1.find();
boolean haspic2=m2.find();
boolean haspic3=m3.find();
if(s1.length()<6 || haspic1==false || haspic2==false || haspic3==false) {
System.out.println("您的密码不符合复杂性要求（密码长度不少于6个字符，至少有一个小写字母，至少有一个大写字母，至少一个数字），请重新输入：");
continue;
}
System.out.println("请输入确认密码:");
String s2=scan.nextLine();
if(s1.equals(s2)) {
t=1;
break;
}
else {
System.out.println("两次密码不一致,请重新输入!");
}
}
}
if(s.equals(password)==false) {
System.out.println("密码不正确，请重新输入:");
}
if(t==1) {
break;
}
}
if(t==1) {
String sql="update tuser set password=? where userName=?";
PreparedStatement ps=con.prepareStatement(sql);
ps.setString(1, s1);
ps.setString(2, user);
int count=ps.executeUpdate();
System.out.println("修改成功!");
ps.close();
}
}
//数据导出
public static void dataoutput(Connection con,String password,String user) throws Exception{
int choose=menu3();
while(true) {
switch(choose) {
case 1:WriteExcel(con,password,user);break;//导出到excel文件
case 2: WriteTXT(con,password,user);break;//导出到文本文件
case 3:return;
default: System.out.println("输入无效，请输入1-3：");break;
}
choose=menu3();
}
}
//导入Excel文件
public static void WriteExcel(Connection con,String password,String user) throws Exception{
ArrayList<String> strlist=new ArrayList<String>();
int counts=0;

String[] str=s.split(" ");
String sql1="select * from tsaledetail";
PreparedStatement ps1=con.prepareStatement(sql1);
ResultSet rs1=ps1.executeQuery();
Date date=new Date();
SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
String datetime=format.format(date);
String[] list=datetime.split("-");
WritableWorkbook wwb = Workbook.createWorkbook(new File("./saleDetail"+list[0]+list[1]+list[2]+".xls"));
WritableSheet ws = wwb.createSheet("明细表", 0);
for(int i=0;i<7;i++) {
Label labelC = new Label(i, 0, str[i]);
ws.addCell(labelC);
}
while(rs1.next()) {
Date date2=rs1.getDate("saletime");
String datetime1=rs1.getString("saletime");
String datetime2=format.format(date2);
//System.out.println(datetime2);
if(datetime2.equals(datetime)) {
String lsh=rs1.getString("lsh");
String braCode=rs1.getString("barCode");
String productName=rs1.getString("productName");
Float price=rs1.getFloat("price");
int count=rs1.getInt("count");
String operator=rs1.getString("operator");
String saletime=rs1.getString("saleTime");
String all=lsh+","+braCode+","+productName+","+price+","+count+","+operator+","+saletime;
strlist.add(all);
counts++;
}
}
for(int j=1;j<=strlist.size();j++) {
String s1=strlist.get(j-1);
String[] strlist2=s1.split(",|，");
for(int i=0;i<7;i++) {
Label labelC = new Label(i, j, strlist2[i]);
ws.addCell(labelC);
}
}
ps1.close();
rs1.close();
wwb.write();
wwb.close();
System.out.println("成功导出"+counts+"条数据到excel文件中");
}
//导入TXT文件
public static void WriteTXT(Connection con,String password,String user) throws Exception{
ArrayList<String> strlist=new ArrayList<String>();
int counts=0;

String str2="--------------------------------------------------------";
String sql1="select * from tsaledetail";
PreparedStatement ps1=con.prepareStatement(sql1);
ResultSet rs1=ps1.executeQuery();
Date date=new Date();
SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
String datetime=format.format(date);
String[] list=datetime.split("-");
File fis=new File("./saleDetail"+list[0]+list[1]+list[2]+".txt");
BufferedWriter bw=new BufferedWriter(new FileWriter(fis));
bw.write(s);
bw.newLine();
bw.write(str2);
bw.newLine();
while(rs1.next()) {
Date date2=rs1.getDate("saletime");
String datetime1=rs1.getString("saletime");
String datetime2=format.format(date2);
if(datetime2.equals(datetime)) {
String lsh=rs1.getString("lsh");
String braCode=rs1.getString("barCode");
String productName=rs1.getString("productName");
Float price=rs1.getFloat("price");
int count=rs1.getInt("count");
String operator=rs1.getString("operator");
String saletime=rs1.getString("saleTime");
String all=lsh+"\t"+braCode+"\t"+productName+"\t"+price+"\t"+count+"\t"+operator+"\t"+saletime;
strlist.add(all);
counts++;
bw.write(all);
bw.newLine();
}
}
ps1.close();
rs1.close();
bw.close();
System.out.println("成功导出"+counts+"条数据到TXT文件中");
}
public static int menu(String chrName) {
int choose;
System.out.println("===图书系统===");
System.out.println("1.借书");
System.out.println("2.查询统计");
System.out.println("3.维护");
System.out.println("4.修改密码");
System.out.println("5.数据导出");
System.out.println("6.退出");
System.out.println("当前管理员:"+chrName);
System.out.println("请输入1-6:");
Scanner scan=new Scanner(System.in);
choose=scan.nextInt();
return choose;
}
public static int menu2() {
int choose;
System.out.println("===图书管理维护===");
System.out.println("1.从excel中导入数据");
System.out.println("2.从文本文件导入数据");
System.out.println("3.键盘输入");
System.out.println("4.按商品名称查询");
System.out.println("5.返回主菜单");
System.out.println("请输入1-5:");
Scanner scan=new Scanner(System.in);
choose=scan.nextInt();
return choose;
}
public static int menu3() {
int choose;
System.out.println("===信息导出====");
System.out.println("1.导出到excel文件");
System.out.println("2.导出到文本文件");
System.out.println("3.返回主菜单");
System.out.println("请选择1-3：");
Scanner scan=new Scanner(System.in);
choose=scan.nextInt();
return choose;
}


}