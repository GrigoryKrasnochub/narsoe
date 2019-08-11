package win.grishanya.narsoe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import win.grishanya.narsoe.dataClasses.Calls;

import static android.provider.CallLog.Calls.INCOMING_TYPE;
import static android.provider.CallLog.Calls.MISSED_TYPE;
import static android.provider.CallLog.Calls.OUTGOING_TYPE;
import static android.provider.CallLog.Calls.REJECTED_TYPE;

public class RecentCallsRecycleViewAdapter  extends RecyclerView.Adapter<RecentCallsRecycleViewAdapter.recentCallsRecycleViewHolder> {
    private ArrayList<Calls> listOfRecentCalls = new ArrayList<>();
    private ArrayList<Calls> listOfRecentCallsFull = new ArrayList<>();
    private String previousStringQuery = "";
    private ArrayList<Calls> listOfSearchedRecentCalls = new ArrayList<>();
    private ItemClickListener itemClickListener;

    //Здесь описывается вьюха
    public class recentCallsRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener {
        ItemClickListener itemClickListener;
        TextView callerName;
        TextView callerPhone;
        TextView callDate;
        ImageView callTypeIcon;

        public recentCallsRecycleViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            callerName =  (TextView) itemView.findViewById(R.id.callerNameTextView);
            callerPhone = (TextView) itemView.findViewById(R.id.phoneNumbertextView);
            callDate = (TextView) itemView.findViewById(R.id.callDateTextView);
            callTypeIcon  =(ImageView) itemView.findViewById(R.id.callTypeImageView);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Calls call = getCall(getAdapterPosition());
            itemClickListener.onItemClick(call.number);
        }

        @Override
        public boolean onLongClick(View v) {
            Calls call = getCall(getAdapterPosition());
            itemClickListener.onItemLongClick(call.number);
            return true;
        }
    }

    //Конструктор
    public RecentCallsRecycleViewAdapter(ArrayList<Calls> listOfRecentCalls,ItemClickListener itemClickListener){
        this.listOfRecentCalls.addAll(listOfRecentCalls);
        listOfRecentCallsFull.addAll(listOfRecentCalls);
        this.itemClickListener = itemClickListener;
    }

    //Назначили верстку
    @NonNull
    @Override
    public RecentCallsRecycleViewAdapter.recentCallsRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_calls_recycle_view_item,viewGroup,false);
        recentCallsRecycleViewHolder myViewHolder = new recentCallsRecycleViewHolder (v,itemClickListener);
        return myViewHolder;
    }

    //Раскладываем данные по виджетам
    @Override
    public void onBindViewHolder(@NonNull RecentCallsRecycleViewAdapter.recentCallsRecycleViewHolder recentCallsRecycleViewHolder, int i) {
        Calls calls = this.listOfRecentCalls.get(i);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss ");
        String date  = dateFormat.format( calls.date );
        recentCallsRecycleViewHolder.callDate.setText(date);
        recentCallsRecycleViewHolder.callerName.setText(calls.name.toString());
        recentCallsRecycleViewHolder.callerPhone.setText(calls.number.toString());
        switch (calls.type){
            case INCOMING_TYPE :{
                recentCallsRecycleViewHolder.callTypeIcon.setImageResource(android.R.drawable.sym_call_incoming);
                break;
            }
            case MISSED_TYPE :{
                recentCallsRecycleViewHolder.callTypeIcon.setImageResource(android.R.drawable.sym_call_missed);
                break;
            }
            case OUTGOING_TYPE :{
                recentCallsRecycleViewHolder.callTypeIcon.setImageResource(android.R.drawable.sym_call_outgoing);
                break;
            }
            case REJECTED_TYPE :{
                recentCallsRecycleViewHolder.callTypeIcon.setImageResource(android.R.drawable.sym_call_missed);
                break;
            }
            default:{
                recentCallsRecycleViewHolder.callTypeIcon.setImageResource(android.R.drawable.sym_action_call);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listOfRecentCalls.size();
    }

    public void updateListOfRecentCalls(ArrayList<Calls> calls,boolean searchIsActive){
        if(searchIsActive){
            showSearchInTheList(previousStringQuery);
        }else{
            listOfRecentCalls.clear();
            listOfRecentCalls.addAll(calls);
        }
        listOfRecentCallsFull.clear();
        listOfRecentCallsFull.addAll(calls);
        notifyDataSetChanged();
    }

    private void updateListOfRecentCallsWithoutChangesInFull(ArrayList<Calls> calls){
        this.listOfRecentCalls.clear();
        this.listOfRecentCalls.addAll(calls);
        notifyDataSetChanged();
    }

    public void showSearchInTheList(String searchQuery){
       if(!searchQuery.equals(previousStringQuery)){
       //Если отличие на один символ, то можно перебирать по тому что внутри. Сейчас криво и медленно
           listOfSearchedRecentCalls.clear();
           previousStringQuery = searchQuery;
       }
        if (searchQuery.length() != 0) {

            for (Calls call : listOfRecentCallsFull) {
                if (searchQuery.matches("\\s*\\+?(\\d{1,11})?\\s*")) {
                    if (call.number.contains(searchQuery)) {
                        listOfSearchedRecentCalls.add(call);
                    }
                } else {
                    if (call.name.toLowerCase().contains(searchQuery.toLowerCase())) {
                        listOfSearchedRecentCalls.add(call);
                    }
                }
            }

            if (!listOfRecentCalls.equals(listOfSearchedRecentCalls)) {
                updateListOfRecentCallsWithoutChangesInFull(listOfSearchedRecentCalls);
            }
        }else{
            //If list has been changed
            if (!listOfRecentCalls.equals(listOfRecentCallsFull)) {
                updateListOfRecentCallsWithoutChangesInFull(listOfRecentCallsFull);
            }
        }
    }

    private Calls getCall (int position){
        return listOfRecentCalls.get(position);
    }

    public interface ItemClickListener {
        void onItemLongClick(String number);
        void onItemClick(String number);
    }
}
