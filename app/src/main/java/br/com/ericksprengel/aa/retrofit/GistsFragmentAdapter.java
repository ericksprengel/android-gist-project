package br.com.ericksprengel.aa.retrofit;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.ericksprengel.aa.retrofit.model.Gist;

public class GistsFragmentAdapter extends BaseAdapter {
    private List<Gist> mGists;
    private LayoutInflater mInflater;
    private DateFormat mDateFormat;


    public GistsFragmentAdapter(List<Gist> gists, LayoutInflater inflater) {
        this.mGists = gists;
        this.mInflater = inflater;
        this.mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public void setGists(List<Gist> gists) {
        this.mGists = gists;
        this.notifyDataSetChanged();
    }

    public void remove(Gist gist) {
        mGists.remove(gist);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mGists == null) {
            return 0;
        } else {
            return mGists.size();
        }
    }

    @Override
    public Object getItem(int pos) {
        if (mGists == null) {
            return null;
        } else {
            return mGists.get(pos);
        }
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.app_fragment_gists_item, parent, false);
            holder = new ViewHolder();
            holder.owner_avatar = (ImageView) convertView.findViewById(R.id.owner_avatar);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.owner_login = (TextView) convertView.findViewById(R.id.owner_login);
            holder.files_count = (TextView) convertView.findViewById(R.id.files_count);
            holder.created_at = (TextView) convertView.findViewById(R.id.created_at);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Gist gist = mGists.get(pos);
        if(gist.owner != null) {
            Picasso.with(mInflater.getContext()).load(gist.owner.avatar_url).into(holder.owner_avatar);
            holder.owner_login.setText(gist.owner.login);
        } else {
            Picasso.with(mInflater.getContext()).load(R.mipmap.ic_launcher).into(holder.owner_avatar);
            holder.owner_login.setText("--//--");
        }
        holder.description.setText(gist.description);
        holder.files_count.setText(String.valueOf(gist.files.size()));
        holder.created_at.setText(mDateFormat.format(gist.created_at));

        return convertView;
    }

    static class ViewHolder {
        ImageView owner_avatar;
        TextView description;
        TextView owner_login;
        TextView files_count;
        TextView created_at;

    }
}
