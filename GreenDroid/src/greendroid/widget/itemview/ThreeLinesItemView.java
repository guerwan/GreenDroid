package greendroid.widget.itemview;

import greendroid.widget.item.Item;
import greendroid.widget.item.ThreeLinesItem;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyrilmottier.android.greendroid.R;

public class ThreeLinesItemView extends RelativeLayout implements ItemView {

    private TextView mOneLeft;
    private TextView mOneRight;
    private TextView mTwoLeft;
    private TextView mTwoRight;
    private TextView mThreeLeft;
    private TextView mThreeRight;

    public ThreeLinesItemView(Context context) {
        this(context, null);
    }

    public ThreeLinesItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void prepareItemView() {
        mOneLeft = (TextView) findViewById(R.id.gd_one_left);
        mOneRight = (TextView) findViewById(R.id.gd_one_right);
        mTwoLeft = (TextView) findViewById(R.id.gd_two_left);
        mTwoRight = (TextView) findViewById(R.id.gd_two_right);
        mThreeLeft = (TextView) findViewById(R.id.gd_three_left);
        mThreeRight = (TextView) findViewById(R.id.gd_three_right);
    }

    public void setObject(Item object) {
        final ThreeLinesItem item = (ThreeLinesItem) object;
        mOneLeft.setText(item.oneLeft);
        mOneRight.setText(item.oneRight);
        mTwoLeft.setText(item.twoLeft);
        mTwoRight.setText(item.twoRight);
        mThreeLeft.setText(item.threeLeft);
        mThreeRight.setText(item.threeRight);
    }

}
