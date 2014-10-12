package de.fuelmeup.ui.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import de.fuelmeup.R;

public class LabelledSeekBar extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {
    private TextView tvLabelLeft;
    private TextView tvLabelRight;
    private SeekBar seekBar;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;
    private String unit;

    public LabelledSeekBar(Context context) {
        super(context);
    }

    public LabelledSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LabelledSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.labelled_seek_bar, this);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        tvLabelLeft = (TextView) findViewById(R.id.label_left);
        tvLabelRight = (TextView) findViewById(R.id.label_right);

        seekBar.setOnSeekBarChangeListener(this);

        applyAttributes(context, attrs);
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LabelledSeekBar,
                0, 0);

        String labelLeft;
        String labelRight;
        try {
            labelLeft = a.getString(R.styleable.LabelledSeekBar_labelRight);
            labelRight = a.getString(R.styleable.LabelledSeekBar_labelLeft);
            unit = a.getString(R.styleable.LabelledSeekBar_unit);
            if (unit == null) {
                unit = "";
            }
        } finally {
            a.recycle();
        }

        tvLabelLeft.setText(labelLeft);
        tvLabelRight.setText(labelRight);
    }

    public void setProgress(int progress) {
        seekBar.setProgress(progress);
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvLabelRight.setText(progress + unit);

        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onStartTrackingTouch(seekBar);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onStopTrackingTouch(seekBar);
        }
    }
}
