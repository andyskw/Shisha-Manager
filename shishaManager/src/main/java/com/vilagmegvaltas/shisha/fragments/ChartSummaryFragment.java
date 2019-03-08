package com.vilagmegvaltas.shisha.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.R;
import com.vilagmegvaltas.shisha.SummaryActivity;
import com.vilagmegvaltas.shisha.entities.Player;
import com.vilagmegvaltas.shisha.entities.Session;

public class ChartSummaryFragment extends Fragment {
	private static Session session;
	private LinearLayout ll;
	Bitmap myChartBitmap;

	public static Fragment newInstance(Session s) {
		ChartSummaryFragment f = new ChartSummaryFragment();
		ChartSummaryFragment.session = s;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((SummaryActivity) getActivity()).setChartFragmentIdentifier(getTag());
		View v = inflater.inflate(R.layout.session_chart, null);

		ll = (LinearLayout) v.findViewById(R.id.contentOfChart);
		ll.addView(execute(getActivity()));
		super.onCreate(savedInstanceState);
		final View host = v.findViewById(R.id.session_chart_llayout_host);

		host.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						host.setDrawingCacheEnabled(true);
						host.buildDrawingCache(true);
						myChartBitmap = host.getDrawingCache();
					}
				});

		FlurryAgent.logEvent("UsageSummary(Chart) viewed");

		return v;
	}

	public Bitmap getChartBitmap() {
		return myChartBitmap;
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	private View execute(Context context) {
		List<Player> players = session.getPlayers();
		String[] titles = new String[players.size()];
		int i = 0;
		for (Player p : players) {
			titles[i++] = p.getName();
		}
		;
		double maxValue = 0;
		List<double[]> x = new ArrayList<double[]>();
		List<double[]> values = new ArrayList<double[]>();
		int[] colors = new int[players.size()];
		PointStyle[] styles = new PointStyle[players.size()];
		Random r = new Random(555555);
		for (i = 0; i < titles.length; i++) {
			colors[i] = Color.rgb(r.nextInt(255), r.nextInt(255),
					r.nextInt(255));
			styles[i] = PointStyle.CIRCLE;
			Player p = players.get(i);
			Map<Integer, Long> history = p.getHistory();
			double[] playerstat = new double[history.size()];
			double[] timing = new double[history.size()];
			int u = 0;
			for (Integer time : history.keySet()) {
				timing[u] = time;
				double value = history.get(time).doubleValue();
				playerstat[u] = value / 1000;
				if (playerstat[u] > maxValue) {
					maxValue = playerstat[u];
				}
				u++;
			}
			x.add(timing);
			values.add(playerstat);
		}

		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
		setChartSettings(renderer, getString(R.string.usageTimes),
				getString(R.string.round), getString(R.string.timesec), 0.0,
				session.getRounds() + 0.5, 0, maxValue + maxValue * 0.2,
				Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);

		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(getResources().getColor(
				R.color.halfTransparent));
		renderer.setMarginsColor(getResources().getColor(
				R.color.halfTransparent));

		GraphicalView chart = ChartFactory.getLineChartView(context,
				buildDataset(titles, x, values), renderer);
		chart.setBackgroundColor(Color.TRANSPARENT);
		return chart;

	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected XYMultipleSeriesDataset buildDataset(String[] titles,
			List<double[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i]);
			double[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

}
