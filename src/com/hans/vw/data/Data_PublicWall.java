package com.hans.vw.data;

import java.util.ArrayList;

import com.hans.vw.activity.wall.Brick;
import com.hans.vw.activity.wall.POI;

public class Data_PublicWall {

	public static ArrayList<Brick> bricks = new ArrayList<Brick>();
	public static final String assets_folder = "publicwall";
	static {

		bricks.add(new Brick(0, 0, "金水桥，离主席像很近啊！~", assets_folder + "/04.jpg",
				300, "起义的农民", 6000, true));
		bricks.add(new Brick(0, 0, "The biggest square in the world!",
				assets_folder + "/02.jpg", 1000, "Jim Green", 10000, true));
		bricks.add(new Brick(0, 0, "马上就去瞻仰毛主席遗容，	人很多啊~！", assets_folder
				+ "/01.jpg", 100, "小蜜蜂", 600, false));
		bricks.add(new Brick(0, 0, "午门，皇帝打大臣板子的地方。", assets_folder + "/03.jpg",
				5000, "王老夫子", 1000, true));
		bricks.add(new Brick(0, 0, "熬夜等着看升旗！", assets_folder + "/04.jpg", 100,
				"大胖兔", 3000, false));

		bricks.add(new Brick(0, 0, "Hans 我爱北京天安门，到此一游！纪念！！", assets_folder
				+ "/05.jpg", 10, "Hans", 100, true));

	}

	public static ArrayList<POI> pois = new ArrayList<POI>();

	static {
		pois.add(new POI(0, 0, "天安门",
				"天安门坐落在中国北京市中心，故宫的南端，与天安门广场隔长安街相望，是明清两代北京皇城的正门。", 100, 4.5,
				false, "", 1302, false, 15));
		pois.add(new POI(
				0,
				0,
				"北海北门",
				"北海公园(Beihai Park)，位于北京市中心区，城内景山西侧，在故宫的西北面，与中海、南海合称三海。属于中国古代皇家园林。",
				100, 4.5, false, "", 2300, false, 15));
		pois.add(new POI(
				0,
				0,
				"景山东门",
				"煤山，现名景山公园，地处北京城的中轴线上，占地32.3公顷，原为元、明、清三代的皇家御苑。煤山高耸峻拔，树木蓊郁，风光壮丽，为北京城内登高远眺，观览全城景致的最佳之处。",
				800, 4.0, false, "", 2000, false, 20));
		pois.add(new POI(0, 0, "无名胡同", "这里的糖葫芦很好吃！", 200, 4.0, true, "大胖兔", 34,
				false, 12));
		pois.add(new POI(0, 0, "观落日地点", "这里可以看到香山！", 500, 2.0, true, "小闹腾", 12,
				false, 32));
		pois.add(new POI(0, 0, "老北京面馆", "炸酱面，面多，酱没有！", 300, 5, true, "Hans",
				1203, false, 10));

	}

}
