package greendroid.graphics.drawable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.StateSet;

import com.cyrilmottier.android.greendroid.R;


public class DockDrawable extends BitmapDrawable {
    private ColorFilter mNormalCf;
    private ColorFilter mAltCf;
    private ColorFilter mUnselectedCf;
    
    public DockDrawable(Resources res, int resId) {
        this(res, res.getDrawable(resId), Color.WHITE, res.getColor(R.color.gd_dock_item_clicked),
        		res.getColor(R.color.gd_dock_item_unselected));
    }
    
    public DockDrawable(Resources res, Drawable d) {
        this(res, d, Color.WHITE, res.getColor(R.color.gd_dock_item_clicked), res.getColor(R.color.gd_dock_item_unselected));
    }

    public DockDrawable(Resources res, int resId, int normalColor, int clickedColor, int unselectedColor) {
        this(res, res.getDrawable(resId), normalColor, clickedColor, unselectedColor);
    }

    public DockDrawable(Resources res, Drawable d, int normalColor, int clickedColor, int unselectedColor) {
    	super((d instanceof BitmapDrawable) ? ((BitmapDrawable) d).getBitmap() : null);
        
    	try {
            Method method = BitmapDrawable.class.getMethod(
                    "setTargetDensity", new Class[] { DisplayMetrics.class } );
            try {
				method.invoke(this, res.getDisplayMetrics());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        } catch (NoSuchMethodException nsme) {
        }
        
    	mNormalCf = new LightingColorFilter(Color.BLACK, normalColor);
        mAltCf = new LightingColorFilter(Color.BLACK, clickedColor);
        mUnselectedCf = new LightingColorFilter(Color.BLACK, unselectedColor);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
    	ColorFilter cf;
    	final boolean useAlt = StateSet.stateSetMatches(DrawableStateSet.ENABLED_PRESSED_STATE_SET, stateSet)
                || StateSet.stateSetMatches(DrawableStateSet.ENABLED_FOCUSED_STATE_SET, stateSet);
        if (useAlt)
        {
        	cf = mAltCf;
            
        }
        else
        {
        	final boolean selected = 
        		StateSet.stateSetMatches(DrawableStateSet.ENABLED_SELECTED_STATE_SET, stateSet);
        	if (!selected)
        		cf = mUnselectedCf;
        	else
        		cf = mNormalCf;
        }
        setColorFilter(cf);
        return true;
    }
}
