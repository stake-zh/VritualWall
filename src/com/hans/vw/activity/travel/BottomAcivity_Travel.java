package com.hans.vw.activity.travel;

import java.util.ArrayList;
import java.util.List;

import com.hans.vw.R;

import android.content.Intent;
import android.os.Bundle;

public class BottomAcivity_Travel extends Activity_Bottom {
	List<ItemView> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private void initViews() {
		if (items != null)
			return;
		items = new ArrayList<ItemView>();
		ItemView item_route = new ItemView(BottomAcivity_Travel.this);
		Intent intent_dialogue = new Intent(context, Activtiy_Route.class);
		intent_dialogue.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		item_route.setContent(intent_dialogue);
		item_route.setIndicate(R.drawable.icon_tab_route, "旅途日记");

		ItemView item_brisks = new ItemView(BottomAcivity_Travel.this);
		Intent intent_history = new Intent(context, Activity_SelfBricks.class);
		intent_history.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		item_brisks.setContent(intent_history);
		item_brisks.setIndicate(R.drawable.icon_tab_bricks, "我的足迹");

		items.add(item_route);
		items.add(item_brisks);
	}

	@Override
	protected List<ItemView> setItems() {
		// TODO Auto-generated method stub
		initViews();
		return items;
	}

}
