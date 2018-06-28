package z.pint.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by DN on 2018/6/28.
 * 为RecyclerView增加间距
 * 预设2列，如果是3列，则左右值不同
 */

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int space = 0;
    private int pos;

    public RecyclerViewItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.top = space;

        //该View在整个RecyclerView中位置。
        pos = parent.getChildAdapterPosition(view);

        //取模

        //两列的左边一列
        if (pos % 2 == 0) {
            outRect.left = space;
            outRect.right = space / 2;
        }

        //两列的右边一列
        if (pos % 2 == 1) {
            outRect.left = space / 2;
            outRect.right = space;
        }
    }
}
