package com.example.elearningmobile.fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningmobile.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FilterBottomFragment extends BottomSheetDialogFragment {
    BottomSheetDialog dialog;


    private FilterFragment filterFragment;
    BottomSheetBehavior<View> bottomSheetBehavior ;
    View rootView ;
    RadioGroup rgLevels ;

    CheckBox cb_free_filter, cb_paid_filter, cb_45_filter, cb_40_filter, cb_35_filter, cb_30_filter;
    TextView btn_reset_filter;
    ImageView iv_dismiss_filter;
    Button btn_apply_filter;

    Boolean[] free;

    List<Float> rating = new ArrayList<>();
    public FilterBottomFragment(FilterFragment filterFragment) {
        this.filterFragment = filterFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_filter_sheet, container, false);
        setControl(rootView);
        setEvent();
        return rootView;
    }

    private void setEvent() {
        rgLevels.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String[] level = new String[1];
                if (checkedId == R.id.rb_beginner_filter) {
                    level[0] = "Beginner";
                } else if (checkedId == R.id.rb_all_levels_filter) {
                    level[0] = "AllLevel";
                } else if (checkedId == R.id.rb_intermediate_filter) {
                    level[0] = "Intermediate";
                } else if (checkedId == R.id.rb_expert_filter) {
                    level[0] = "Expert";
                }
                filterFragment.setLevel(level);
            }
        });

        cb_free_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                free[0] = isChecked;
                filterFragment.setFree(free);
            }
        });


        cb_paid_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                free[1] = isChecked;
                filterFragment.setFree(free);
            }
        });

        cb_45_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    rating.add(4.5f);
                }else {
                    removeRating(4.5f);
                }

                filterFragment.setRating(getArrayOfRating());
            }
        });

        cb_40_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    rating.add(4.0f);
                }else {
                    removeRating(4.0f);
                }
                filterFragment.setRating(getArrayOfRating());

            }
        });

        cb_35_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    rating.add(3.5f);
                }else {
                    removeRating(3.5f);
                }
                filterFragment.setRating(getArrayOfRating());

            }
        });

        cb_30_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    rating.add(3.0f);
                } else {
                    removeRating(3.0f);
                }
                filterFragment.setRating(getArrayOfRating());
            }
        });
    }

    void removeRating(Float rat) {
        for(Float r : rating) {
            if (r.equals(rat)) {
                rating.remove(r);
            }
        }
    }


    Float[] getArrayOfRating() {

        int sizeRating = rating.size();
        if (sizeRating > 0) {

            Float[] newRating = new Float[sizeRating];
            int start = 0 ;
            for (Float r : rating) {
                newRating[start++] = r;
            }
        }

        return new Float[];
    }

    private void setControl(View rootView) {
        cb_free_filter = rootView.findViewById(R.id.cb_free_filter);
        cb_paid_filter = rootView.findViewById(R.id.cb_paid_filter);
        cb_45_filter = rootView.findViewById(R.id.cb_45_filter);
        cb_40_filter = rootView.findViewById(R.id.cb_40_filter);
        cb_35_filter = rootView.findViewById(R.id.cb_35_filter);
        cb_30_filter = rootView.findViewById(R.id.cb_30_filter);
        btn_reset_filter = rootView.findViewById(R.id.btn_reset_filter);
        iv_dismiss_filter = rootView.findViewById(R.id.iv_dismiss_filter);
        btn_apply_filter = rootView.findViewById(R.id.btn_apply_filter);
        rgLevels = rootView.findViewById(R.id.rg_levels);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        LinearLayout layout = dialog.findViewById(R.id.ll_bottomSheet);

        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
    }
}
