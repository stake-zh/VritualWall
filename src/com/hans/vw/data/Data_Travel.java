package com.hans.vw.data;

import java.util.ArrayList;
import java.util.Date;

import com.ctsi.utils.DataUtil;
import com.ctsi.utils.DateUtil;
import com.hans.vw.activity.travel.Route;
import com.hans.vw.activity.travel.SelfBrick;

public class Data_Travel {
	public static ArrayList<SelfBrick> bricks = new ArrayList<SelfBrick>();
	public static final String assets_folder = "travel";

	static int mLon1 = 116398312;
	static int mLat1 = 39939592;
	static int mLon2 = 116402794;
	static int mLat2 = 39930979;
	static int mLon3 = 116403438;
	static int mLat3 = 39923343;
	static int mLon4 = 116404044;
	static int mLat4 = 39914424;
	private static int mLon5 = 116405511;
	private static int mLat5 = 39910876;
	private static int mLon6 = 116414813;
	private static int mLat6 = 39904712;

	static {
		bricks.add(new SelfBrick("Hans", "北海", DateUtil.String2Date(
				"2013-10-01 10:01:00").getTime(), mLat1, mLon1, "北海看海",
				assets_folder + "/20.jpg", true));
		bricks.add(new SelfBrick("Hans", "景山山顶", DateUtil.String2Date(
				"2013-10-02 11:31:00").getTime(), mLat2, mLon2, "在这里可以俯瞰整个北京城",
				assets_folder + "/25.jpg", true));
		bricks.add(new SelfBrick("Hans", "故宫太和殿", DateUtil.String2Date(
				"2013-10-03 13:15:00").getTime(), mLat3, mLon3, "皇帝你好！皇帝再见！",
				assets_folder + "/21.jpg", true));
		bricks.add(new SelfBrick("Hans", "天安门", DateUtil.String2Date(
				"2013-10-04 17:43:00").getTime(), mLat4, mLon4, "看升旗，感觉很帅气啊！",
				assets_folder + "/22.jpg", true));
		bricks.add(new SelfBrick("Hans", "人民英雄纪念碑", DateUtil.String2Date(
				"2013-10-05 18:03:00").getTime(), mLat5, mLon5, "人民英雄永垂不朽",
				assets_folder + "/23.jpg", true));
		bricks.add(new SelfBrick("Hans", "前门全聚德", DateUtil.String2Date(
				"2013-10-06 19:15:00").getTime(), mLat6, mLon6, "烤鸭不错，略肥！",
				assets_folder + "/24.jpg", true));

	}

	public static ArrayList<Route> routes = new ArrayList<Route>();

	static {
		routes.add(new Route(DateUtil.String2Date("2013-10-07 07:13:00")
				.getTime(), "2013国庆假期", DateUtil.String2Date(
				"2013-10-01 09:24:00").getTime(), DateUtil.String2Date(
				"2013-10-07 19:32:00").getTime(), bricks, "大胖兔"));

		routes.add(new Route(DateUtil.String2Date("2013-02-17 20:22:00")
				.getTime(), "2013海南之行", DateUtil.String2Date(
				"2013-02-08 13:24:00").getTime(), DateUtil.String2Date(
				"2013-02-16 23:32:00").getTime(), bricks, "Hans"));

		routes.add(new Route(DateUtil.String2Date("2012-12-27 16:43:00")
				.getTime(), "2012圣诞夜", DateUtil.String2Date(
				"2012-12-24 17:24:00").getTime(), DateUtil.String2Date(
				"2012-12-25 03:18:00").getTime(), bricks, "游侠"));

		routes.add(new Route(DateUtil.String2Date("2012-10-15 17:37:00")
				.getTime(), "2012国庆假期", DateUtil.String2Date(
				"2012-10-01 07:24:00").getTime(), DateUtil.String2Date(
				"2012-10-07 21:32:00").getTime(), bricks, "路人甲"));

	}

}
