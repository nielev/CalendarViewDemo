package com.neo.calendarviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neo.calendarviewdemo.R;
import com.neo.calendarviewdemo.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 日历自定义view
 * Created by Neo on 2017/1/16.
 */

public class CalendarView extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private Context mContext;
    private ViewPager mVp_calendar;
    private int curYear;
    private int curMonth;
    private static final String TAG = "CalendarView";
    private static final int SPACE_DEFAULT = -1;
    private int selectedPosition;
    private AttributeSet mAttrs;

    private TextView mTv_month_showname;

    private TextView mTv_now;

    private ImageView mIv_left;

    private ImageView mIv_right;
    private float mCell_height;

    public CalendarView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAttrs(attrs);
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        initAttrs(attrs);
        initView();
    }
    private void initAttrs (AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCell_height = ta.getDimension(R.styleable.CalendarView_cell_hight, SPACE_DEFAULT);
    }

    private void initView() {
        View.inflate(mContext, R.layout.view_calendar, this);
        mTv_month_showname = (TextView) findViewById(R.id.tv_month_showname);
        mTv_now = (TextView) findViewById(R.id.tv_now);
        mIv_left = (ImageView) findViewById(R.id.iv_left);
        mIv_right = (ImageView) findViewById(R.id.iv_right);

        initCalendar();
    }

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        //月份从0开始
        curMonth = calendar.get(Calendar.MONTH) + 1;

        mTv_month_showname.setText(CalendarUtils.getCurrentMonthName(mContext));

        mTv_now.setOnClickListener(this);
        mIv_left.setOnClickListener(this);
        mIv_right.setOnClickListener(this);

        mVp_calendar = (ViewPager) findViewById(R.id.vp_calendar);

        //添加分割线
        mVp_calendar.setAdapter(new CalendarAdapter());
        mVp_calendar.setCurrentItem(curPosition);
        selectedPosition = curPosition;
        mVp_calendar.addOnPageChangeListener(this);
    }

    private View calendarView(List<String> months, String fistWeekDay){
        View view = View.inflate(mContext, R.layout.view_calendar_content, null);
        RecyclerView rv_calendar = (RecyclerView) view.findViewById(R.id.rv_calendar);
        //设置布局管理器
        rv_calendar.setLayoutManager(new GridLayoutManager(mContext, 7));
        //设置adapters
        rv_calendar.setAdapter(new MonthAdapter(months, fistWeekDay));
        //设置Item增加、移除动画
        rv_calendar.setItemAnimator(new DefaultItemAnimator());
        rv_calendar.addItemDecoration(new SpaceItemDecoration(SPACE_DEFAULT, SPACE_DEFAULT, SPACE_DEFAULT, CalendarUtils.dp2px(0, mContext), 1, 0xff000000));
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            selectedPosition = position;
            String ym = map.get(position);
            String[] split = ym.split(",");
            mTv_month_showname.setText(CalendarUtils.getMonthName(mContext, Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_now:
                mVp_calendar.setCurrentItem(curPosition);
                break;
            case R.id.iv_left:
                mVp_calendar.setCurrentItem(selectedPosition-1);
                break;
            case R.id.iv_right:
                mVp_calendar.setCurrentItem(selectedPosition+1);
                break;
        }
    }


    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int left, top, right, bottom;
        private int mDividerHight = 1;
        private Paint mColorPaint;
        private Drawable mDividerDrawable;

        public SpaceItemDecoration(int left, int top, int right, int bottom, int dividerHight, int dividerColor){
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.mDividerHight = dividerHight;
            mColorPaint = new Paint();
            mColorPaint.setColor(dividerColor);
        }
        public SpaceItemDecoration(int left, int top, int right, int bottom, int dividerHight, Drawable dividerDrawable){
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.mDividerHight = dividerHight;
            this.mDividerDrawable = dividerDrawable;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            // TODO: 2017/1/18 drawline
//            drawHorizontalDivider(c, parent);
//            drawVerticalDivider(c, parent);
        }

        private void drawVerticalDivider(Canvas c, RecyclerView parent) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getTop() - params.topMargin;
                int bottom = child.getBottom() + params.bottomMargin;

                int left = 0;
                int right = 0;
                //左边第一列
                if((i % 3) == 0) {
                    //item左边分割线
                    left = child.getLeft();
                    right = left + mDividerHight;
                    if (null != mDividerDrawable) {
                        mDividerDrawable.setBounds(left, top, right, bottom);
                        mDividerDrawable.draw(c);
                    }
                    if (mColorPaint != null) {
                        c.drawRect(left, top, right, bottom, mColorPaint);
                    }
                    //item右边分割线
                    left = child.getRight() + params.rightMargin - mDividerHight;
                    right = left + mDividerHight;
                } else {
                    //非左边第一列
                    left = child.getRight() + params.rightMargin - mDividerHight;
                    right = left + mDividerHight;
                }
                //画分割线
                if (null != mDividerDrawable) {
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(c);
                }
                if (mColorPaint != null) {
                    c.drawRect(left, top, right, bottom, mColorPaint);
                }

            }
        }

        private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                final int left = child.getLeft() - params.leftMargin - mDividerHight;
                final int right = child.getRight() + params.rightMargin;
                int top = 0;
                int bottom = 0;

                // 最上面一行
                if ((i / 3) == 0) {
                    //当前item最上面的分割线
                    top = child.getTop();
                    //当前item下面的分割线
                    bottom = top + mDividerHight;
                    if (null != mDividerDrawable) {
                        mDividerDrawable.setBounds(left, top, right, bottom);
                        mDividerDrawable.draw(c);
                    }
                    if (mColorPaint != null) {
                        c.drawRect(left, top, right, bottom, mColorPaint);
                    }
                    top = child.getBottom() + params.bottomMargin;
                    bottom = top + mDividerHight;
                } else {
                    top = child.getBottom() + params.bottomMargin;
                    bottom = top + mDividerHight;
                }
                //画分割线
                if (null != mDividerDrawable) {
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(c);
                }
                if (mColorPaint != null) {
                    c.drawRect(left, top, right, bottom, mColorPaint);
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildLayoutPosition(view);
            if(left != SPACE_DEFAULT) {
                outRect.left = left;
            }
            if(top != SPACE_DEFAULT) {
                outRect.top = top;
            }
            if(right != SPACE_DEFAULT) {
                outRect.right = right;
            }

            if(bottom != SPACE_DEFAULT) {
                outRect.bottom = bottom;
            }
        }
    }

    class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.DayViewHolder> {
        private List<String> mlist;
        private List<String> weekList = new ArrayList<>();
        private String[] arrays;
        public MonthAdapter (List<String> list,String fisrtWeekDay) {
            arrays = mContext.getResources().getStringArray(R.array.weeks);

            int i = 0;
            int count = 0;
            for (String day : arrays) {
                weekList.add(day.substring(0, 3));
                if (day.equals(fisrtWeekDay)) {
                    count = i;
                } else {
                    i++;
                }
            }
            for (int x = 0; x < count; x++) {
                weekList.add("");
            }

            this.mlist = list;
            mlist.addAll(0, weekList);
        }

        @Override
        public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            DayViewHolder holder = new DayViewHolder(View.inflate(mContext, R.layout.layout_day, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(DayViewHolder holder, int position) {
            holder.tv.setText(mlist.get(position));
            ViewGroup.LayoutParams layoutParams = holder.tv.getLayoutParams();
            if(mCell_height != SPACE_DEFAULT) {
                layoutParams.height = (int) mCell_height;
            }
            holder.tv.setLayoutParams(layoutParams);

        }

        @Override
        public int getItemCount() {
            return mlist.size();
        }
        class DayViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            public DayViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv_day);
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }



    private int curPosition;
    private Map<Integer,String> map;
    class CalendarAdapter extends PagerAdapter {
        public CalendarAdapter() {
            map = new ArrayMap<>();
            curPosition = (curYear - 1) * 12 + curMonth;
        }

        @Override
        public int getCount() {
            int count = curPosition + 12000;//默认包含以前所有的日期和后面一千年
            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int dPosition = position - curPosition;
            int dYear = dPosition / 12;
            int dMonth = dPosition % 12;
            int goalYear;
            int goalMonth;
            if ((curMonth + dMonth) <= 0) {
                goalYear = curYear + dYear -1;
                goalMonth = curMonth + dMonth + 12;
            } else {
                if (curMonth + dMonth > 12) {
                    goalYear = curYear + dYear + 1;
                    goalMonth = curMonth + dMonth - 12;
                } else {
                    goalYear = curYear + dYear;
                    goalMonth = curMonth + dMonth;
                }
            }
            map.put(position,goalYear+","+goalMonth);
            View view = calendarView(CalendarUtils.getMonthDaysList(goalYear, goalMonth), CalendarUtils.getDayOfWeekByDate(mContext, CalendarUtils.intToString(goalYear, goalMonth, 1)));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 设置单个控件高度
     * @param dimen
     */
    public void setCellHight (int dimen) {
        mCell_height = dimen;
    }
}
