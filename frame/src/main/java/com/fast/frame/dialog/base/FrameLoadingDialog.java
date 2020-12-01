package com.fast.frame.dialog.base;

import com.fast.frame.dialog.BaseNiceDialog;
import com.fast.frame.dialog.ViewHolder;
import com.fast.frame.interrface.ILoadingDialog;
import com.fast.library.R;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.ToolUtils;
import com.fast.library.utils.UIUtils;

import androidx.fragment.app.FragmentManager;

/**
 * 说明：FrameLoadingDialog
 *
 * @author xiaomi
 */
public class FrameLoadingDialog extends BaseNiceDialog implements ILoadingDialog {

    private String loadingText;

    @Override
    public ILoadingDialog setCancel(boolean isCancel) {
        setOutCancel(isCancel);
        return this;
    }

    @Override
    public ILoadingDialog setText(String text) {
        loadingText = text;
        if (StringUtils.isEmpty(loadingText)) {
            loadingText = UIUtils.getString(R.string.def_loading_text);
        }
        return this;
    }

    @Override
    public ILoadingDialog showDialog(FragmentManager manager) {
        show(manager);
        return this;
    }

    @Override
    public boolean getBindActivity() {
        return ToolUtils.isNotFinish(getActivity());
    }

    @Override
    public void dismissDialog() {
        super.dismiss();
        dismiss();
    }

    public static ILoadingDialog newInstance() {
        FrameLoadingDialog dialog = new FrameLoadingDialog();
        dialog.setDimAmount(0.3f);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.fast_frame_def_dialog_loading;
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        holder.setText(R.id.tv_loading_text, loadingText);
    }

}
