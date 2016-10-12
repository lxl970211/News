package com.zzptc.liuxiaolong.news.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.NetWorkStatus;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_dialog__write_comment)
public class FragmentDialog_WriteComment extends DialogFragment implements OnRequestResultListener {



    @ViewInject(R.id.et_write_comment)
    private EditText et_write_comment;
    @ViewInject(R.id.tv_cancel)
    private TextView mCancel;
    @ViewInject(R.id.tv_sendComment)
    private TextView mSendComment;
    private PushData pushData;

    //评论完成后监听
    private OnRequestResultListener onRequestResultListener;

    public void setOnRequestResultListener(OnRequestResultListener listener){
        onRequestResultListener = listener;
    }

    public static FragmentDialog_WriteComment newInstance(String url, String title){
        FragmentDialog_WriteComment fragmentDialog_writeComment = new FragmentDialog_WriteComment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        fragmentDialog_writeComment.setArguments(bundle);
        return fragmentDialog_writeComment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = x.view().inject(this, inflater, container);

        return v;
    }

    //点击监听
    @Event(value = {R.id.tv_cancel, R.id.tv_sendComment})
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();

                break;

            case R.id.tv_sendComment:
                if (NetWorkStatus.getNetWorkType(getContext()) != 0) {

                        if (et_write_comment.getText().toString() != null && !et_write_comment.getText().toString().equals("")) {

                            Bundle bundle = getArguments();

                            String newsId = bundle.getString("url");

                            String title = bundle.getString("title");
                            String content = et_write_comment.getText().toString();

                            pushData = new PushData(getContext());
                            pushData.writeComment(content, newsId, title);

                            pushData.setOnRequestResultListener(this);
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "请输入评论内容", Toast.LENGTH_SHORT).show();
                        }

                }else{
                    Toast.makeText(getContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


    @Override
    public void OnGetRequestResultStatusListener(int status) {
        switch (status){
            case ResultCodes.COMMENT_AUCCESS:
                Toast.makeText(x.app(), "评论成功", Toast.LENGTH_SHORT).show();
                if (onRequestResultListener != null){
                    onRequestResultListener.OnGetRequestResultStatusListener(3);
                }
                break;

            case ResultCodes.COMMENT_ERROR:
                Toast.makeText(x.app(), "评论失败", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
