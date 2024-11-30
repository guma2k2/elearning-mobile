package com.example.elearningmobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningmobile.R;
import com.example.elearningmobile.model.AnswerVM;
import com.example.elearningmobile.model.CourseDetailType;

import java.util.List;

public class AnswerRecycleAdapter extends RecyclerView.Adapter<AnswerRecycleAdapter.AnswerHolder>{

    private List<AnswerVM> answerVMS;

    public AnswerRecycleAdapter(List<AnswerVM> answerVMS) {
        this.answerVMS = answerVMS;
    }

    @NonNull
    @Override
    public AnswerRecycleAdapter.AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        return new AnswerRecycleAdapter.AnswerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerRecycleAdapter.AnswerHolder holder, int position) {
        AnswerVM answerVM = answerVMS.get(position);
        holder.rb_answerText.setText(answerVM.getAnswerText());
    }

    @Override
    public int getItemCount() {
        return answerVMS != null ?  answerVMS.size() : 0 ;
    }

    class AnswerHolder extends RecyclerView.ViewHolder{
        RadioButton rb_answerText;

        public AnswerHolder(@NonNull View itemView) {
            super(itemView);
            rb_answerText = itemView.findViewById(R.id.rb_answerText);
        }
    }
}
