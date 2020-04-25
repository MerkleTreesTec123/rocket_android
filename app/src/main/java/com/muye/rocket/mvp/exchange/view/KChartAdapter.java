package com.muye.rocket.mvp.exchange.view;

import com.github.tifezh.kchartlib.chart.BaseKChartAdapter;
import com.muye.rocket.entity.exchange.KLineEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KChartAdapter extends BaseKChartAdapter {

    private List<KLineEntity> datas = new ArrayList<>();

    public KChartAdapter() {

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public Date getDate(int position) {
        try {
            Date date = new Date();
            date.setTime(datas.get(position).Date);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向头部添加数据
     */
    public void addHeaderData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(data);
            DataHelper.calculate(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部添加数据
     */
    public void addFooterData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(0, data);
            DataHelper.calculate(datas);
            notifyDataSetChanged();
        }
    }

    public void setDatas(List<KLineEntity> data) {
        datas.clear();
        if (data != null && data.size() > 0) {
            datas.addAll(data);
            DataHelper.calculate(datas);
        }
        notifyDataSetChanged();
    }

    /**
     * 改变某个点的值
     *
     * @param position 索引值
     */
    public void changeItem(int position, KLineEntity data) {
        datas.set(position, data);
        notifyDataSetChanged();
    }

}
