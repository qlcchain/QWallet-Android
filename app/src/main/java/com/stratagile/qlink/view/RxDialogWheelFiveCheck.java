package com.stratagile.qlink.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.dialog.dialogWheel.DateArrayAdapter;
import com.vondear.rxtools.view.dialog.dialogWheel.NumericWheelAdapter;
import com.vondear.rxtools.view.dialog.dialogWheel.OnWheelChangedListener;
import com.vondear.rxtools.view.dialog.dialogWheel.WheelView;

import java.util.Calendar;

public class RxDialogWheelFiveCheck extends RxDialog {
    private Context context;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private int curYear;
    private int curMonth;
    private int curDay;
    private TextView tv_sure;
    private TextView tv_cancle;
    private CheckBox checkBox_day;
    private Calendar calendar;
    private String months[] = new String[]{"01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private String hours[] = new String[]{"00","01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
            "14","15", "16", "17", "18", "19",
            "20", "21", "22", "23"};
    private String days[] = new String[]{"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    private String minutes[] = new String[]{"00", "01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
            "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43",
            "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55",
            "56", "57", "58", "59"};

    private int beginYear = 0;
    private int endYear = 0;
    private int divideYear = endYear - beginYear;
    private WheelView hour;
    private WheelView minute;
    private int curHour;

    public int getBeginYear() {
        return beginYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getDivideYear() {
        return divideYear;
    }

    public RxDialogWheelFiveCheck(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        build();
    }

    public RxDialogWheelFiveCheck(Context context, int beginYear) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.beginYear = beginYear;
        build();
    }

    public RxDialogWheelFiveCheck(Context context, int beginYear, int endYear) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.beginYear = beginYear;
        this.endYear = endYear;
        build();
    }

    public RxDialogWheelFiveCheck(Context context, TextView tv_time) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        build();
        tv_time.setText(curYear + "year" + months[curMonth] + "month");
    }

    private void build() {
        calendar = Calendar.getInstance();
        final View dialogView1 = LayoutInflater.from(context).inflate(R.layout.dialog_five_check, null);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);//, hour, minute
            }
        };

        curYear = calendar.get(Calendar.YEAR);
        if (beginYear == 0) {
            beginYear = curYear - 5;
        }
        if (endYear == 0) {
            endYear = curYear + 5;
        }
        if (beginYear > endYear) {
            endYear = beginYear;
        }

        //year
        year = (WheelView) dialogView1.findViewById(R.id.wheelView_year);
        year.setBackgroundResource(com.vondear.rxtools.R.drawable.transparent_bg);
        year.setWheelBackground(com.vondear.rxtools.R.drawable.transparent_bg);
        year.setWheelForeground(com.vondear.rxtools.R.drawable.wheel_val_holo);
        year.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
        year.setViewAdapter(new DateNumericAdapter(context, beginYear, endYear, endYear - beginYear - 5));
        year.setCurrentItem(endYear - beginYear - 5);
        year.addChangingListener(listener);


        // month
        month = (WheelView) dialogView1
                .findViewById(R.id.wheelView_month);
        month.setBackgroundResource(com.vondear.rxtools.R.drawable.transparent_bg);
        month.setWheelBackground(com.vondear.rxtools.R.drawable.transparent_bg);
        month.setWheelForeground(com.vondear.rxtools.R.drawable.wheel_val_holo);
        month.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
        curMonth = calendar.get(Calendar.MONTH);
        month.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);


        //day
        day = (WheelView) dialogView1.findViewById(R.id.wheelView_day);
        updateDays(year, month, day);
        curDay = calendar.get(Calendar.DAY_OF_MONTH);
        day.setCurrentItem(curDay - 1);
        day.setBackgroundResource(com.vondear.rxtools.R.drawable.transparent_bg);
        day.setWheelBackground(com.vondear.rxtools.R.drawable.transparent_bg);
        day.setWheelForeground(com.vondear.rxtools.R.drawable.wheel_val_holo);
        day.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);

        hour = (WheelView) dialogView1.findViewById(R.id.wheelView_hour);
        hour.setBackgroundResource(com.vondear.rxtools.R.drawable.transparent_bg);
        hour.setWheelBackground(com.vondear.rxtools.R.drawable.transparent_bg);
        hour.setWheelForeground(com.vondear.rxtools.R.drawable.wheel_val_holo);
        hour.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
        curHour = calendar.get(Calendar.HOUR_OF_DAY);
        hour.setViewAdapter(new DateArrayAdapter(context, hours, calendar.get(Calendar.HOUR_OF_DAY)));
        hour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
        hour.addChangingListener(listener);

        minute = (WheelView) dialogView1.findViewById(R.id.wheelView_minute);
        minute.setBackgroundResource(com.vondear.rxtools.R.drawable.transparent_bg);
        minute.setWheelBackground(com.vondear.rxtools.R.drawable.transparent_bg);
        minute.setWheelForeground(com.vondear.rxtools.R.drawable.wheel_val_holo);
        minute.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
        minute.setViewAdapter(new DateArrayAdapter(context, minutes, calendar.get(Calendar.MINUTE)));
        minute.setCurrentItem(calendar.get(Calendar.MINUTE));
        minute.addChangingListener(listener);

        tv_sure = (TextView) dialogView1.findViewById(com.vondear.rxtools.R.id.tv_sure);
        tv_cancle = (TextView) dialogView1.findViewById(com.vondear.rxtools.R.id.tv_cancel);

        checkBox_day = (CheckBox) dialogView1.findViewById(com.vondear.rxtools.R.id.checkBox_day);
        checkBox_day.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    day.setVisibility(View.VISIBLE);
                } else {
                    day.setVisibility(View.GONE);
                }
            }
        });

        getLayoutParams().gravity = Gravity.CENTER;
        setContentView(dialogView1);
    }


    public WheelView getDay() {
        return day;
    }

    public int getCurDay() {
        return curDay;
    }

    public CheckBox getCheckBox_day() {
        return checkBox_day;
    }

    public WheelView getYear() {
        return year;
    }

    public WheelView getMonth() {
        return month;
    }
    public WheelView getHour() {
        return hour;
    }
    public WheelView getMinute() {
        return minute;
    }

    private int getCurYear() {
        return curYear;
    }

    private int getCurMonth() {
        return curMonth;
    }

    public TextView getTv_sure() {
        return tv_sure;
    }

    public TextView getTv_cancle() {
        return tv_cancle;
    }

    private String[] getMonths() {
        return months;
    }

    private String[] getHours() {
        return hours;
    }
    private String[] getMinutes() {
        return minutes;
    }
    private String[] getDays() {
        return days;
    }

    public int getSelectorYear() {
        return beginYear + getYear().getCurrentItem();
    }

    public String getSelectorMonth() {
        return getMonths()[getMonth().getCurrentItem()];
    }

    public String getSelectorDay() {
        return getDays()[getDay().getCurrentItem()];
    }
    public String getSelectorHour() {
        return getHours()[getHour().getCurrentItem()];
    }
    public String getSelectorMinute() {
        return getMinutes()[getMinute().getCurrentItem()];
    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    private void updateDays(WheelView year, WheelView month, WheelView day) {  //, WheelView hour, WheelView minute
        calendar.set(Calendar.YEAR, beginYear + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxDays = RxTimeTool.getDaysByYearMonth(beginYear + year.getCurrentItem(), month.getCurrentItem() + 1);
        day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);


    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            /*if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
			}*/
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
