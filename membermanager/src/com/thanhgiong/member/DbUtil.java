package com.thanhgiong.member;

public class DbUtil {
	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS membertb (id integer primary key autoincrement, name varchar(125), dob varchar(30), addr varchar(300), gt varchar(10), phone varchar (125), nbinary BLOB)";
	public static String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS usertb (id integer primary key autoincrement, uid varchar(30), upwd varchar(30))";
	public static String DB_NAME = "memberdb";
	public static String TB_USER_NAME = "usertb";
	public static String TB_MEMBER_NAME = "membertb";
	
}
