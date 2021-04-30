package com.newkoad.deliver;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.newkoad.deliver.R;


public class BadgeTabLayout extends TabLayout {

    private final SparseArray<Builder> mTabBuilders = new SparseArray<>();

    public BadgeTabLayout(Context context) {    super(context);    }
    public BadgeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BadgeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {      super(context, attrs, defStyleAttr);    }

    public Builder with(int position) {
        Tab tab = getTabAt(position);
        return with(tab);
    }


    public Builder with(Tab tab) {
        if (tab == null) {
            throw new IllegalArgumentException("null");
        }
        Builder builder = mTabBuilders.get(tab.getPosition());
        if (builder == null) {
            builder = new Builder(this, tab);
            mTabBuilders.put(tab.getPosition(), builder);
        }
        return builder;
    }










    public static final class Builder {

        private static final int INVALID_NUMBER = Integer.MIN_VALUE;

        @Nullable
        final View mView;

        final Context mContext;

        final TabLayout.Tab mTab;

        @Nullable
        TextView mBadgeTextView;

        @Nullable
        ImageView mIconView;

        @Nullable
        TextView mTextView;

        Drawable mIconDrawable;

        Integer mIconColorFilter;

        int mBadgeCount = Integer.MIN_VALUE;

        boolean mHasBadge = false;

        boolean mHasText = false;

        String mText;


        private Builder(TabLayout parent, @NonNull TabLayout.Tab tab) {
            super();

            this.mContext = parent.getContext();

            this.mTab = tab;

            if (tab.getCustomView() != null) {

                this.mView = tab.getCustomView();

            } else {

                this.mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_custom_icon, parent, false);

            }

            if (mView != null) {

                this.mIconView = (ImageView) mView.findViewById(R.id.tab_icon);

                this.mBadgeTextView = (TextView) mView.findViewById(R.id.tab_badge);

                this.mTextView = (TextView) mView.findViewById(R.id.tab_text);

            }

            if (this.mBadgeTextView != null) {

                if(mBadgeCount > 0 ) {

                    this.mHasBadge = mBadgeTextView.getVisibility() == View.VISIBLE;
                    try {

                        this.mBadgeCount = Integer.parseInt(mBadgeTextView.getText().toString());

                    } catch (NumberFormatException er) {

                        er.printStackTrace();

                        this.mBadgeCount = INVALID_NUMBER;
                    }
                }
            }

            if (this.mIconView != null) {
                mIconDrawable = mIconView.getDrawable();
            }

            if(this.mTextView != null){
                this.mHasText = mTextView.getVisibility() == View.VISIBLE;
            }
        }


        public Builder hasBadge() {
            mHasBadge = true;
            return this;
        }


        public Builder noBadge() {
            mHasBadge = false;
            return this;
        }



        public Builder badge(boolean hasBadge) {
            mHasBadge = hasBadge;
            return this;
        }
        public Builder text(boolean hasText) {
            mHasText = hasText;
            return this;
        }


        public Builder icon(int drawableRes) {
            mIconDrawable = getDrawableCompat(mContext, drawableRes);
            return this;
        }


        public Builder icon(Drawable drawable) {
            mIconDrawable = drawable;
            return this;
        }


        public Builder iconColor(Integer color) {
            mIconColorFilter = color;
            return this;
        }


        public Builder setText(String text){
            mText = text;
            return this;
        }

        public Builder increase() {
            mBadgeCount =
                    mBadgeTextView == null ?
                            INVALID_NUMBER
                            :
                            Integer.parseInt(mBadgeTextView.getText().toString()) + 1;
            return this;
        }


        public Builder decrease() {
            mBadgeCount =
                    mBadgeTextView == null ?
                            INVALID_NUMBER
                            :
                            Integer.parseInt(mBadgeTextView.getText().toString()) - 1;
            return this;
        }


        public Builder badgeCount(int count) {
            mBadgeCount = count;
            return this;
        }

        public void build() {
            if (mView == null) {
                return;
            }


            if (mBadgeTextView != null) {
                mBadgeTextView.setText(formatBadgeNumber(mBadgeCount));

                if (mHasBadge) {
                    mBadgeTextView.setVisibility(View.VISIBLE);
                } else {

                    mBadgeTextView.setVisibility(View.INVISIBLE);
                }
            }


            if (mIconView != null && mIconDrawable != null) {
                mIconView.setImageDrawable(mIconDrawable.mutate());

                if (mIconColorFilter != null) {
                    mIconDrawable.setColorFilter(mIconColorFilter, PorterDuff.Mode.MULTIPLY);
                }
            }


            if(mText != null ){
                mTextView.setText(mText);
            }

            mTab.setCustomView(mView);
        }
    }

    private static Drawable getDrawableCompat(Context context, int drawableRes) {
        Drawable drawable = null;
        try {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = context.getResources().getDrawable(drawableRes);
            } else {
                drawable = context.getResources().getDrawable(drawableRes, context.getTheme());
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return drawable;
    }



    private static String formatBadgeNumber(int value) {
        if (value == 0) {

            return "" ;
        }
        return Integer.toString(value);

    }


    private static String formatBadgeNumber2(int value) {
        if (value == 0) {

            return "" ;
        }
        return Integer.toString(value);

    }




}
