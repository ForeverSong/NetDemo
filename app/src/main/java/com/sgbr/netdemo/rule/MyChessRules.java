package com.sgbr.netdemo.rule;

import com.sgbr.netdemo.myview.ChessView;

public class MyChessRules {
    /**
     * 通过传入的(x, y)坐标判断是否为合法点击的点
     *
     * @param x 坐标x
     * @param y 坐标y
     * @return 一个合法点击
     */
    public static boolean getValidPoint(int x, int y) {
        if (x == 0 || y == 0 || x == ChessView.MAX_LINE || y == ChessView.MAX_LINE) {
            return false;
        }
        boolean flag = false;
        switch (x) {
            case 1:
            case 7:
                switch (y) {
                    case 1:
                    case 4:
                    case 7:
                        flag = true;
                        break;
                }
                break;
            case 2:
            case 6:
                switch (y) {
                    case 2:
                    case 4:
                    case 6:
                        flag = true;
                        break;
                }
                break;
            case 3:
            case 5:
                switch (y) {
                    case 3:
                    case 4:
                    case 5:
                        flag = true;
                        break;
                }
                break;
            case 4:
                switch (y) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                        flag = true;
                        break;
                }
                break;

        }

        return flag;
    }
}
