package mn.zaal.zaal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import mn.zaal.zaal.MapsActivity;
import mn.zaal.zaal.R;
import mn.zaal.zaal.models.zaalModel;

public class FragOneRecyclerView extends RecyclerView.Adapter<FragOneRecyclerView.ViewHolder> {

    private final List<zaalModel> list;
    private final Context context;

    public FragOneRecyclerView(Context context, List<zaalModel> list) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressWarnings("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final zaalModel modelDB = list.get(position);
        holder.bind(modelDB);
        holder.itemView.setOnClickListener(v -> {
            String lat = modelDB.getMap_lat();
            String lng = modelDB.getMap_lng();
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView expandableTitle;
        public final TextView expandablePriceTal;
        public final TextView expandablePriceButen;
        private final ImageView expandableImage;
        //TODO private final RelativeLayout sub_item;

        public ViewHolder(View itemView) {
            super(itemView);
            expandableTitle = itemView.findViewById(R.id.expandableTitle);
            expandablePriceTal = itemView.findViewById(R.id.expandablePriceTal);
            expandablePriceButen = itemView.findViewById(R.id.expandablePriceButen);
            expandableImage = itemView.findViewById(R.id.expandableImage);
          //TODO  sub_item = itemView.findViewById(R.id.sub_item);
        }

        @SuppressLint("SetTextI18n")
        private void bind(zaalModel subject) {
            // Get the state
            boolean expanded = subject.isExpanded();
            // Set the visibility based on state
            // TODO sub_item.setVisibility(expanded ? View.VISIBLE : View.GONE);
            expandableTitle.setText(subject.getName());
            expandablePriceTal.setText(subject.getPrice_tal()+"₮");
            expandablePriceButen.setText(subject.getPrice_buten()+"₮");
            String url = "http://sys.opgo1.cc/";
            Glide.with(context)
                    .load(url + subject.getZaal_pic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(expandableImage);
        }
    }
}
