/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vilagmegvaltas.shisha;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.R;
import com.vilagmegvaltas.shisha.charts.AbstractChart;
import com.vilagmegvaltas.shisha.entities.Player;
import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.utils.FlurryAPIKeyContainer;
import com.vilagmegvaltas.shisha.utils.IntentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

/**
 * Average temperature demo chart.
 */
public class UsageSummary extends AbstractChart {
	private LinearLayout ll;
	private Session s;

	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		s = (Session) (getIntent().getExtras().get("session"));
		setContentView(R.layout.session_chart);
		ll = (LinearLayout) findViewById(R.id.contentOfChart);
		ll.addView(execute(this));
		super.onCreate(savedInstanceState);
		final View host = findViewById(R.id.session_chart_llayout_host);
		
		host.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						
						host.setDrawingCacheEnabled(true);
						host.buildDrawingCache(true);
						Bitmap x = host.getDrawingCache();
						Intent share = (Intent) getIntent().getExtras().get(
								"share");
						share.putExtra("usage_summary_graph", x);

					}
				});

	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	private View execute(Context context) {
		List<Player> players = s.getPlayers();
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
				s.getRounds() + 0.5, 0, maxValue + maxValue * 0.2,
				Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		return ChartFactory.getLineChartView(context,
				buildDataset(titles, x, values), renderer);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent main = IntentManager.getStartSessionIntent(this);
			finish();
			startActivity(main);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryAPIKeyContainer.API_KEY);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
