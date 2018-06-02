package com.example.shinji.navigationdrawer;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static com.example.shinji.navigationdrawer.R.array.drawer_items;


public class MainActivity extends AppCompatActivity {

	private DrawerLayout mDrawerLayout;
	private View mDrawerView;
	private ActionBarDrawerToggle mDrawerToggle;
	private Toolbar mToolbar;
	private String[] mNavigationTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ・ツールバー
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		// ツールバーのセット
		setSupportActionBar(mToolbar);

		// ・レイアウトとドロワー
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		// 1. ActionBarDrawerToggleクラスのインスタンスを作成し、
		// DrawerLayoutにsetDrawerListener()でセットする
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                 /* Navigation Drawerを持つActivity */
				mDrawerLayout,        /* DrawerLayout オブジェクト */
				mToolbar, /* Navigation Drawer Indicator */
				R.string.drawer_open, /* "open drawer" の説明 */
				R.string.drawer_close /* "close drawer" の説明 */)
		{
			// ドロワーが開いたとき
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				// onPrepareOptionsMenu()が呼ばれるようにする
				invalidateOptionsMenu();
			}
			// ドロワーが閉じたとき
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// onPrepareOptionsMenu()が呼ばれるようにする
				invalidateOptionsMenu();
			}
		};
		// レイアウトにドロワーを登録
		mDrawerLayout.addDrawerListener(mDrawerToggle);

		// ドロワービューの設定
		mDrawerView = findViewById(R.id.drawer_frame);

		setupNavDrawerList();

		selectItem(0);

	}

	/***
	 * ドロワービューにリストを設定
	 * クリックしたら、selectItem
	 */
	private void setupNavDrawerList() {
		mNavigationTitles = getResources().getStringArray(drawer_items);
		ListView listView = (ListView) mDrawerView;

		// ListViewにアダプターをセット
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mNavigationTitles));

		// ListViewがタップされたときのリスナーをセット
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {
				selectItem(position);
			}
		});
	}

	/** コンテンツ部分のFragmentを入れ替える */
	private void selectItem(int position) {
		// 入れ替えるFragmentを作成
		Fragment fragment = MainFragment.getInstance(mNavigationTitles[position]);
		// replace()メソッドで入れ替え
		getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// Navigation Drawerを閉じる
		mDrawerLayout.closeDrawer(mDrawerView);
	}

	// トグルボタン作成時
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.w( "DEBUG_DATA", "onPostCreate");
		// 状態のSync
		mDrawerToggle.syncState();
	}

}
