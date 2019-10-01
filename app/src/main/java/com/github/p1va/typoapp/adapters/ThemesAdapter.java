package com.github.p1va.typoapp.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.p1va.typoapp.R;
import com.github.p1va.typoapp.models.Theme;

import java.util.List;
import java.util.Locale;

/**
 * Created by Stefano Piva on 27/07/2018.
 */
public class ThemesAdapter extends RecyclerView.Adapter<ThemesAdapter.ViewHolder> {

    private List<Theme> mThemes;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public ThemesAdapter(Context context, List<Theme> themes) {
        this.mInflater = LayoutInflater.from(context);
        this.mThemes = themes;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.theme_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theme theme = getItem(position);

        holder.textView.setTextColor(theme.getFontColor());
        holder.textView.setBackgroundColor(theme.getBackgroundColor());

        // Get typeface
        AssetManager am = mContext.getAssets();
        Typeface typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", theme.font));

        holder.textView.setText(theme.miniatureText);
        holder.textView.setTypeface(typeface);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mThemes.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //View fontColorView;
        //View backgroundColorView;

        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.theme_row_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Theme getItem(int id) {
        return mThemes.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}