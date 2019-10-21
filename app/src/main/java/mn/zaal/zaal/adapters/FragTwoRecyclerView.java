package mn.zaal.zaal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import mn.zaal.zaal.MapsActivity;
import mn.zaal.zaal.R;
import mn.zaal.zaal.ZaalActivity;
import mn.zaal.zaal.models.zaalModel;

public class FragTwoRecyclerView extends RecyclerView.Adapter<FragTwoRecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private List<zaalModel> list;
    private List<zaalModel> FilteredList;

    public FragTwoRecyclerView(Context context, List<zaalModel> list) {
        this.context = context;
        this.list = list;
        this.FilteredList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);
        return new ViewHolder(v);
    }

    @SuppressWarnings("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final zaalModel modelDB = list.get(position);
        holder.bind(modelDB);


        holder.itemView.setOnClickListener(v -> {
            // Get the current state of the item
            boolean expanded = modelDB.isExpanded();
            // Change the state
            modelDB.setExpanded(!expanded);
            // Notify the adapter that item has changed
            notifyItemChanged(position);

            holder.mapButton.setOnClickListener(v1 -> {

                Intent intent = new Intent(context, ZaalActivity.class);
                context.startActivity(intent);
              /*  String lat = modelDB.getMap_lat();
                String lng = modelDB.getMap_lng();
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                context.startActivity(intent); */
            });
        });
    }

    @Override
    public int getItemCount() {
        return FilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    FilteredList = list;
                } else {
                    List<zaalModel> filteredList = new ArrayList<>();
                    for (zaalModel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getDistrict().toLowerCase().contains(charString.toLowerCase()) || row.getDescription().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    FilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FilteredList = (ArrayList<zaalModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView zaalTitle;
        public final TextView price_tal;
        public final TextView price_buten;
        public final TextView zaal_description;
        private final ImageView zaalImageSlider;
        private final RelativeLayout sub_item;
        private final TextView city, district, address, khoroo;
        private final MaterialButton orderButton, mapButton;

        public ViewHolder(View itemView) {
            super(itemView);
            zaalTitle = itemView.findViewById(R.id.zaalTitle);
            price_tal = itemView.findViewById(R.id.price_tal);
            price_buten = itemView.findViewById(R.id.price_buten);
            zaalImageSlider = itemView.findViewById(R.id.zaalImageSlider);
            zaal_description = itemView.findViewById(R.id.zaal_description);
            sub_item = itemView.findViewById(R.id.sub_item);
            city = itemView.findViewById(R.id.city);
            district = itemView.findViewById(R.id.district);
            khoroo = itemView.findViewById(R.id.khoroo);
            address = itemView.findViewById(R.id.address);
            orderButton = itemView.findViewById(R.id.orderButton);
            mapButton = itemView.findViewById(R.id.mapButton);
        }

        @SuppressLint("SetTextI18n")
        private void bind(@NotNull zaalModel subject) {
            // Get the state
            boolean expanded = subject.isExpanded();
            // Set the visibility based on state
            sub_item.setVisibility(expanded ? View.VISIBLE : View.GONE);
            zaalTitle.setText(subject.getName());
            city.setText(subject.getCity());
            district.setText(subject.getDistrict());
            khoroo.setText(subject.getKhoroo());
            price_tal.setText("Нэг талдаа: " + subject.getPrice_tal() + "₮");
            price_buten.setText("Хоёр талдаа: " + subject.getPrice_buten() + "₮");
            address.setText(subject.getAddress());
            String url = "http://sys.opgo1.cc/";
            Glide.with(context)
                    .load(url + subject.getZaal_pic())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(zaalImageSlider);
        }
    }
}