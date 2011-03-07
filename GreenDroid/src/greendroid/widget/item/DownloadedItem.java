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
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.cyrilmottier.android.greendroid.R;

/**
 * A ThumbnailItem item is an complex item that wrap a drawable and two strings
 * : A title and a subtitle. The representation of that item is quite common to
 * Android users: The drawable is on the left of the item view and on the right
 * the title and the subtitle are displayed like a {@link SubtitleItem}.
 * 
 * @author Cyril Mottier
 */
public class DownloadedItem extends SubtitleItem {

    /**
     * The resource ID for the drawable.
     */
    public int likeDrawableId;
    public int dislikeDrawableId;
    public String likeText;
    public String dislikeText;
    public boolean likeChecked;
    public boolean dislikeChecked;
    public OnCheckedChangeListener likeCheckListener;
    public OnCheckedChangeListener dislikeCheckListener;

    /**
     * @hide
     */
    public DownloadedItem() {
    }

    /**
     * @param text
     * @param subtitle
     * @param drawableId
     */
    public DownloadedItem(String text, String subtitle, 
    		int likeDrawableId, int dislikeDrawableId, 
    		String likeText, String dislikeText, 
    		OnCheckedChangeListener likeCheckListener,
    	    OnCheckedChangeListener dislikeCheckListener, 
    	    boolean enabled) {
        super(text, subtitle);
        this.likeDrawableId = likeDrawableId;
        this.dislikeDrawableId = dislikeDrawableId;
        this.likeText = likeText;
        this.dislikeText = dislikeText;
        this.enabled = enabled;
        likeChecked = false;
        dislikeChecked = false;
        this.likeCheckListener = likeCheckListener;
        this.dislikeCheckListener = dislikeCheckListener;
    }

    @Override
    public ItemView newView(Context context, ViewGroup parent) {
        return createCellFromXml(context, R.layout.gd_downloaded_item_view, parent);
    }

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException,
            IOException {
        super.inflate(r, parser, attrs);

        TypedArray a = r.obtainAttributes(attrs, R.styleable.DownloadedItem);
        likeDrawableId = a.getResourceId(R.styleable.DownloadedItem_like, likeDrawableId);
        dislikeDrawableId = a.getResourceId(R.styleable.DownloadedItem_dislike, dislikeDrawableId);
        likeText = a.getString(R.styleable.DownloadedItem_likeText);
        dislikeText = a.getString(R.styleable.DownloadedItem_dislikeText);
        a.recycle();
    }

}
