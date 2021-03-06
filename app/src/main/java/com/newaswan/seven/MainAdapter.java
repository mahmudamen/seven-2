package com.newaswan.seven;

/**
 * Created by Anonymo on 21/11/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyHoder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    Button btnAnim2;
    BadgeView badge4;
    List<PhotoModel> list;
    Context context;
    String vurl,vtitle,vshortdesc,vprice,vrating,vliq,vlongdesc;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public MainAdapter(List<PhotoModel> list, Context context) {
        this.list = list;
        this.context = context;


    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        //   View view = LayoutInflater.from(context).inflate(R.layout.cardsales,parent,false);
        //   MyHoder myHoder = new MyHoder(view);

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardmain, parent, false);
            vh = new MyHoder(v);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardmain, parent, false);
            vh = new MyHoder(v);
        }
        return (MyHoder) vh;
    }

    @Override
    public void onBindViewHolder(final MyHoder holder, int position) {
        final String url = list.get(position).getImage();
        final String title = list.get(position).getTitle();
        final String shortdesc = list.get(position).getShortdesc();
        final String price = list.get(position).getDayx();
        final String rating = list.get(position).getDate();
        final String liq = list.get(position).getLiqo();
        final String longdesc = list.get(position).getLongdesc();


        if (holder instanceof MyHoder) {

            PhotoModel mylist = list.get(position);
            final String Url = mylist.getImage();
            holder.title.setText(mylist.getTitle());
            holder.shordesc.setText(mylist.getShortdesc());
            holder.price.setText(mylist.getDayx());
            holder.rating.setText(mylist.getDate());
            holder.liq.setText(mylist.getLiqo());
            holder.longdesc.setText(mylist.getLongdesc());

            ((MyHoder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vurl = url;
                    vtitle = title;
                    vshortdesc = shortdesc;
                    vprice = price;
                    vrating = rating;
                    vliq = liq;
                    vlongdesc = longdesc;

                    Intent i = new Intent(context,DetailActivity.class);
                    Book book = new Book(vtitle,vshortdesc,vurl,vprice,vrating,vliq,vlongdesc);
                    i.putExtra("textViewTitle",vtitle);
                    i.putExtra("shortdesc",vshortdesc);
                    i.putExtra("imag",vurl);
                    i.putExtra("day",vprice);
                    i.putExtra("date",vrating);
                    i.putExtra("liq",vliq);
                    i.putExtra("longdesc",vlongdesc);
                    i.putExtra("Book",book);
                    context.startActivity(i);
                }
            });
            //  holder.imageView.setImageResource(Integer.parseInt(mylist.getImage()));
            Glide.with(context).load(mylist.getImage()).into(holder.imageView);
        } else {
            ((ProgressViewHolder)list).progressBar.setVisibility(View.VISIBLE);
        }
    }
    public void getSelectedContextMenuItem(MenuItem item){
        this.openDetailActivity(item.getTitle().toString());
    }
    private void openDetailActivity(String choice){
        Intent i = new Intent(context,DetailActivity.class);
        i.putExtra("tile",vtitle);
        i.putExtra("shortdesc",vshortdesc);
        i.putExtra("imag",vurl);
        i.putExtra("day",vprice);
        i.putExtra("date",vrating);

        i.putExtra("liq",vliq);
        i.putExtra("longdesc",vlongdesc);
        context.startActivity(i);
    }
    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main7, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_like:
                   // Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.action_fav:
                  //  Toast.makeText(context, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {

        int arr = 0;
        try{
            if(list.size()==0){

                arr = 0;
            }
            else{
                arr=list.size();
            }
        }catch (Exception e){
        }
        return arr;
    }

    class MyHoder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnCreateContextMenuListener{
        TextView title,shordesc,price,rating,liq,longdesc;
        ImageView imageView,overflow;
        View.OnClickListener itemClickListener;
        final Typeface alyoum = Typeface.createFromAsset(context.getAssets(), "fonts/alyoum.ttf");
        final Typeface agency = Typeface.createFromAsset(context.getAssets(), "fonts/annahar-bold-ar.otf");
        final Typeface boahmed = Typeface.createFromAsset(context.getAssets(), "fonts/boahmed-alhour-ar.ttf");
        public MyHoder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.textViewTitle);
            shordesc= (TextView)itemView.findViewById(R.id.textViewShortDesc);
            price= (TextView)itemView.findViewById(R.id.textViewRating);
            rating= (TextView)itemView.findViewById(R.id.textViewPrice);
            liq= (TextView)itemView.findViewById(R.id.textLiq);
            longdesc= (TextView)itemView.findViewById(R.id.textViewLongDesc);
            imageView = itemView.findViewById(R.id.imageView);
            overflow = itemView.findViewById(R.id.overflow);
            btnAnim2 =  itemView.findViewById(R.id.button1);

            title.setTypeface(boahmed);
            shordesc.setTypeface(alyoum);
            rating.setTypeface(alyoum);
            price.setTypeface(alyoum);
        }
        public void setOnClickListener(View.OnClickListener ic){
            this.itemClickListener=ic;
        }


        public void onClick(View view) {
            this.itemClickListener.onClick(view);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("اذهب الي ....");
            contextMenu.add(0,0,0,"اذهب");
        }
    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }
    public int getItemViewType(int position) {
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

}