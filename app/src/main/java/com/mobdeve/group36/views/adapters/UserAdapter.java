package com.mobdeve.group36.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobdeve.group36.Data.model.User;
import com.mobdeve.group36.R;
import com.mobdeve.group36.views.fragments.GetProfile;
import com.mobdeve.group36.views.main.MessageActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserFragmentHolder> {

    private ArrayList<User> usersArrayList;
    private Context context;
    private GetProfile bottomSheetProfileDetailUser;
    private Boolean isChat;

    public UserAdapter(ArrayList<User> usersArrayList, Context context, Boolean isChat) {
        this.usersArrayList = usersArrayList;
        this.context = context;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public UserFragmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.user_list, parent, false);
        return new UserFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFragmentHolder holder, int position) {
        User users = usersArrayList.get(position);

        String imageUrl = users.getImageUrl();
        String userName = users.getUsername();
        String bio = users.getBio();
        String user_status = users.getStatus();


        if (imageUrl.equals("default")) {
            holder.iv_profile_image.setImageResource(R.drawable.sample_img);
        } else {
            Glide.with(context).load(imageUrl).into(holder.iv_profile_image);
        }

        holder.tv_name.setText(userName);
        holder.iv_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetProfileDetailUser = new GetProfile(userName, imageUrl, bio, context);
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                bottomSheetProfileDetailUser.show(manager, "edit");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", users.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }


    public class UserFragmentHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_profile_image;
        TextView tv_name;
        ImageView iv_status_user_list;

        UserFragmentHolder(@NonNull View itemView) {
            super(itemView);
            iv_profile_image = itemView.findViewById(R.id.profile_image);
            tv_name = itemView.findViewById(R.id.user_name_list);
            iv_status_user_list = itemView.findViewById(R.id.iv_status_user_list);
        }
    }

}
