package com.example.shinji.navigationdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private View mDrawerView;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerView = findViewById(R.id.drawer_frame);

		// 1. ActionBarDrawerToggleクラスのインスタンスを作成し、
		// DrawerLayoutにsetDrawerListener()でセットする

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                 /* Navigation Drawerを持つActivity */
				mDrawerLayout,        /* DrawerLayout オブジェクト */
				R.drawable.ic_drawer, /* Navigation Drawer Indicator */
				R.string.drawer_open, /* "open drawer" の説明 */
				R.string.drawer_close /* "close drawer" の説明 */)
		{
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				// onPrepareOptionsMenu()が呼ばれるようにする
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// onPrepareOptionsMenu()が呼ばれるようにする
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// 2. ホームボタンを使うようにする
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// 3. 必要であれば、DrawerLayoutに影をセットする
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		setupNavDrawerList();

		selectItem(0);

	}

	private String[] mNavigationTitles;

	private void setupNavDrawerList() {
		mNavigationTitles = getResources().getStringArray(R.array.drawer_items);
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

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// 4. onPostCreated()でActionBarDrawerToggleのsyncState()
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// 5. onConfigurationChanged()で
		// ActionBarDrawerToggleのonConfigurationChanged()を呼ぶ
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// 6. onOptionsItemSelected()でActionBarDrawerToggleの
		// onOptionsItemSelected() を呼んで、ホームボタンが押されたときの処理を行う
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// ホームボタン以外の通常のAction Itemの処理を行う

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Navigation Drawerが開いていたら R.id.action_addのメニュー項目を表示しない
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerView);
		menu.findItem(R.id.action_add).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

}
