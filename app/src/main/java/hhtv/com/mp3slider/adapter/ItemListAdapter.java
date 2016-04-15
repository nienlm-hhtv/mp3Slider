package hhtv.com.mp3slider.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hhtv.com.mp3slider.R;
import hhtv.com.mp3slider.model.itemlist.ItemList;

/**
 * Created by nienb on 12/4/16.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>{


    private Picasso p;
    private List<ItemList> models;
    private Context mContext;
    private IOnItemClick callback;

    public ItemListAdapter(List<ItemList> models, Context mContext, IOnItemClick callback) {
        this.models = models;
        this.mContext = mContext;
        this.callback = callback;
        p = Picasso.with(mContext);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ItemList i = models.get(position);
        p.load(i.getImage()).into(holder.i);
        holder.n.setText(i.getName());
        holder.d.setText(i.getDescription());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView i;
        TextView n, d;
        View v;
        public ItemViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            i = (ImageView)itemView.findViewById(R.id.item_image);
            n = (TextView)itemView.findViewById(R.id.item_text_name);
            d = (TextView)itemView.findViewById(R.id.item_text_description);
        }
    }

    public interface IOnItemClick{
        void onItemClick(ItemList i);
    }
}
