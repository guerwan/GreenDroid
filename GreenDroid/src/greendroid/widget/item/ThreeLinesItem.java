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
package greendroid.widget.item;

import greendroid.widget.itemview.ItemView;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.cyrilmottier.android.greendroid.R;

public class ThreeLinesItem extends Item {

	public String oneLeft;
    public String oneRight;
    public String twoLeft;
    public String twoRight;
    public String threeLeft;
    public String threeRight;

    /**
     * @hide
     */
    public ThreeLinesItem() {
    }

    /**
     * Constructs a new ThreeLinesItem with the specified text and subtitle.
     * 
     * @param text The text for this item
     * @param subtitle The item's subtitle
     */
    public ThreeLinesItem(String oneLeft, String oneRight,
	    String twoLeft,
	    String twoRight,
	    String threeLeft,
	    String threeRight,
	    boolean enabled) {
        super();
        this.oneLeft = oneLeft;
        this.oneRight = oneRight;
        this.twoLeft = twoLeft;
        this.twoRight = twoRight;
        this.threeLeft = threeLeft;
        this.threeRight = threeRight;
        this.enabled = enabled;
    }

    @Override
    public ItemView newView(Context context, ViewGroup parent) {
        return createCellFromXml(context, R.layout.gd_three_lines_item_view, parent);
    }

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException,
            IOException {
        super.inflate(r, parser, attrs);

        TypedArray a = r.obtainAttributes(attrs, R.styleable.ThreeLinesItem);
        oneLeft = a.getString(R.styleable.ThreeLinesItem_oneLeft);
        oneRight = a.getString(R.styleable.ThreeLinesItem_oneRight);
        twoLeft = a.getString(R.styleable.ThreeLinesItem_twoLeft);
        twoRight = a.getString(R.styleable.ThreeLinesItem_twoRight);
        threeLeft = a.getString(R.styleable.ThreeLinesItem_threeLeft);
        threeRight = a.getString(R.styleable.ThreeLinesItem_threeRight);
        a.recycle();
    }
}
