/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greendroid.app;

import greendroid.graphics.drawable.DockDrawable;
import greendroid.util.Config;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBar.OnActionBarListener;
import greendroid.widget.ActionBarHost;
import greendroid.widget.ActionBarItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.cyrilmottier.android.greendroid.R;

/**
 * An equivalent to a TabActivity that manages fancy tabs and an ActionBar
 * 
 * @author Cyril Mottier
 */
public class GDTabActivity extends TabActivity implements ActionBarActivity {

    private static final String LOG_TAG = GDTabActivity.class.getSimpleName();

    private ActionBarHost mActionBarHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createLayout());
    }

    public int createLayout() {
        return R.layout.gd_tab_content;
    }

    public GDApplication getGDApplication() {
        return (GDApplication) getApplication();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        onPreContentChanged();
        onPostContentChanged();
    }

    public void onPreContentChanged() {
        mActionBarHost = (ActionBarHost) findViewById(R.id.gd_action_bar_host);
        if (mActionBarHost == null) {
            throw new RuntimeException(
                    "Your content must have an ActionBarHost whose id attribute is R.id.gd_action_bar_host");
        }
        mActionBarHost.getActionBar().setOnActionBarListener(mActionBarListener);
    }

    public void onPostContentChanged() {

        boolean titleSet = false;

        final Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra(ActionBarActivity.GD_ACTION_BAR_TITLE);
            if (title != null) {
                titleSet = true;
                setTitle(title);
            }
        }

        if (!titleSet) {
            // No title has been set via the Intent. Let's look in the
            // ActivityInfo
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), 0);
                if (activityInfo.labelRes != 0) {
                    setTitle(activityInfo.labelRes);
                }
            } catch (NameNotFoundException e) {
                // Do nothing
            }
        }
        
        final int visibility = intent.getIntExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.VISIBLE);
        getActionBar().setVisibility(visibility);
    }

    // @Override
    // protected void onTitleChanged(CharSequence title, int color) {
    // setTitle(title);
    // }

    @Override
    public void setTitle(CharSequence title) {
        getActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    public ActionBar getActionBar() {
        return mActionBarHost.getActionBar();
    }

    public ActionBarItem addActionBarItem(ActionBarItem item) {
        return getActionBar().addItem(item);
    }
    
    public ActionBarItem addActionBarItem(ActionBarItem item, int itemId) {
        return getActionBar().addItem(item, itemId);
    }

    public ActionBarItem addActionBarItem(ActionBarItem.Type actionBarItemType) {
        return getActionBar().addItem(actionBarItemType);
    }
    
    public void addActionBarItem(ActionBarItem item, boolean withDivider) {
        getActionBar().addItem(item, withDivider);
    }
    
    public ActionBarItem addActionBarItem(ActionBarItem.Type actionBarItemType, int itemId) {
        return getActionBar().addItem(actionBarItemType, itemId);
    }

    public FrameLayout getContentView() {
        return mActionBarHost.getContentView();
    }

    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        return false;
    }

    private OnActionBarListener mActionBarListener = new OnActionBarListener() {
        public void onActionBarItemClicked(int position) {
            if (position == OnActionBarListener.HOME_ITEM) {

                final Class<?> klass = getGDApplication().getHomeActivityClass();
                if (klass != null && !klass.equals(GDTabActivity.class.getClass())) {
                    if (Config.GD_INFO_LOGS_ENABLED) {
                        Log.i(LOG_TAG, "Going back to the home activity");
                    }
                    Intent homeIntent = new Intent(GDTabActivity.this, klass);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }

            } else {
                if (!onHandleActionBarItemClick(getActionBar().getItem(position), position)) {
                    if (Config.GD_WARNING_LOGS_ENABLED) {
                        Log.w(LOG_TAG, "Click on item at position " + position + " dropped down to the floor");
                    }
                }
            }
        }
    };

    /*
     * GDTabActivity methods
     */

    public void addTab(String tag, int labelId, Intent intent) {
        addTab(tag, getString(labelId), intent);
    }

    public void addTab(String tag, CharSequence label, Intent intent) {
        final TabHost host = getTabHost();

        View indicator = createTabIndicator(label);
        if (indicator == null) {
            final TextView textIndicator = (TextView) getLayoutInflater().inflate(R.layout.gd_tab_indicator,
                    getTabWidget(), false);
            textIndicator.setText(label);
            indicator = textIndicator;
        }

        TabSpec tabSpec = host.newTabSpec(tag);
        // Using reflection because setIndicator(View) is only available after 1.5 
    	try {
            Method method = TabSpec.class.getMethod(
                    "setIndicator", new Class[] { View.class } );
            try {
				method.invoke(tabSpec, indicator);
				host.addTab(tabSpec.setContent(intent));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        } catch (NoSuchMethodException nsme) {
        	tabSpec.setIndicator(label);
        	host.addTab(tabSpec.setContent(intent));
        	((TextView)host.getTabWidget()
            		.getChildAt(host.getTabWidget().getChildCount() - 1).findViewById(android.R.id.title)).setTextColor(this.getResources().getColorStateList(R.drawable.tab_custom_text_color));
        }
    }
    
    public void addTab(String tag, int labelId, TabContentFactory factory) {
        addTab(tag, getString(labelId), factory);
    }
    
    public void addTab(String tag, CharSequence label, TabContentFactory factory) {
        final TabHost host = getTabHost();

        View indicator = createTabIndicator(label);
        if (indicator == null) {
            final TextView textIndicator = (TextView) getLayoutInflater().inflate(R.layout.gd_tab_indicator,
                    getTabWidget(), false);
            textIndicator.setText(label);
            indicator = textIndicator;
        }
        
        TabSpec tabSpec = host.newTabSpec(tag);
        // Using reflection because setIndicator(View) is only available after 1.5 
    	try {
            Method method = TabSpec.class.getMethod(
                    "setIndicator", new Class[] { View.class } );
            try {
				method.invoke(tabSpec, indicator);
				host.addTab(tabSpec.setContent(factory));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        } catch (NoSuchMethodException nsme) {
        	tabSpec.setIndicator(label);
        	host.addTab(tabSpec.setContent(factory));
        	((TextView)host.getTabWidget()
        		.getChildAt(host.getTabWidget().getChildCount() - 1).findViewById(android.R.id.title)).setTextColor(this.getResources().getColorStateList(R.drawable.tab_custom_text_color));
        }
        
      
    }

    
    public void addImageTab(String tag, int imageDrawable, Intent intent, int labelId) {
        addImageTab(tag, imageDrawable, intent, getString(labelId));
    }

    public void addImageTab(String tag, int imageDrawable, Intent intent,CharSequence label) {
        final TabHost host = getTabHost();

        View indicator = null;
        if (indicator == null) {
            final ImageView imageIndicator = (ImageView) getLayoutInflater().inflate(R.layout.gd_tab_image_indicator,
                    getTabWidget(), false);
            indicator = imageIndicator;
        }
        
        ((ImageView)indicator).setImageDrawable(new DockDrawable(getResources(), imageDrawable));
        TabSpec tabSpec = host.newTabSpec(tag);
        // Using reflection because setIndicator(View) is only available after 1.5 
    	try {
            Method method = TabSpec.class.getMethod(
                    "setIndicator", new Class[] { View.class } );
            try {
				method.invoke(tabSpec, indicator);
				host.addTab(tabSpec.setContent(intent));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        } catch (NoSuchMethodException nsme) {
        	tabSpec.setIndicator(label);
        	host.addTab(tabSpec.setContent(intent));
        	((TextView)host.getTabWidget()
            		.getChildAt(host.getTabWidget().getChildCount() - 1).findViewById(android.R.id.title)).setTextColor(this.getResources().getColorStateList(R.drawable.tab_custom_text_color));
        }
        
        
    }
    

    public void addImageTab(String tag, int imageDrawable, TabContentFactory factory, int labelId) {
    	addImageTab(tag, imageDrawable, factory, getString(labelId));
    }
    
    public void addImageTab(String tag, int imageDrawable, TabContentFactory factory, CharSequence label) {
        final TabHost host = getTabHost();

        View indicator = null;
        if (indicator == null) {
            final ImageView imageIndicator = (ImageView) getLayoutInflater().inflate(R.layout.gd_tab_image_indicator,
                    getTabWidget(), false);
            indicator = imageIndicator;
        }
        
        ((ImageView)indicator).setImageDrawable(new DockDrawable(getResources(), imageDrawable));
        TabSpec tabSpec = host.newTabSpec(tag);
        
        try {
            Method method = TabSpec.class.getMethod(
                    "setIndicator", new Class[] { View.class } );
            try {
				method.invoke(tabSpec, indicator);
				host.addTab(tabSpec.setContent(factory));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        } catch (NoSuchMethodException nsme) {
        	tabSpec.setIndicator(label);
        	host.addTab(tabSpec.setContent(factory));
        	((TextView)host.getTabWidget()
        		.getChildAt(host.getTabWidget().getChildCount() - 1).findViewById(android.R.id.title)).setTextColor(this.getResources().getColorStateList(R.drawable.tab_custom_text_color));
        }
    }

    protected View createTabIndicator(CharSequence label) {
        return null;
    }

}
