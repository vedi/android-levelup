/*
 * Copyright (C) 2012-2014 Soomla Inc.
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

package com.soomla.levelup.scoring;

import com.soomla.SoomlaUtils;
import com.soomla.levelup.data.LUJSONConsts;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A specific type of <code>ScoreId</code> that has an associated range.
 * The score's value can be only inside the range of values.  For example,
 * a shooting score can on a scale of 10 to 100 according to the user's
 * performance in the game.
 * <p/>
 * Created by refaelos on 07/05/14.
 */
public class RangeScore extends Score {


    /**
     * Constructor
     *
     * @param id    see parent
     * @param range the range applicable to this score
     */
    public RangeScore(String id, Range range) {
        super(id);
        this.mRange = range;
    }

    /**
     * Constructor
     *
     * @param id           see parent
     * @param name         see parent
     * @param higherBetter see parent
     * @param range        the range applicable to this score
     */
    public RangeScore(String id, String name, boolean higherBetter, Range range) {
        super(id, name, higherBetter);
        this.mRange = range;

        // if the score is descending, the start value should be
        // the high value, otherwise it's very confusing that the initial
        // score is the lowest
        if (!higherBetter) {
            setStartValue(range.getHigh());
        }
    }

    /**
     * Constructor.
     * Generates an instance of <code>RangeScore</code> from the given <code>JSONObject</code>.
     *
     * @param jsonObject A JSONObject representation of the wanted <code>RangeScore</code>.
     * @throws JSONException
     */
    public RangeScore(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        mRange = new Range(jsonObject.getJSONObject(LUJSONConsts.LU_SCORE_RANGE));

        // if the score is descending, the start value should be
        // the high value, otherwise it's very confusing that the initial
        // score is the lowest
        if (!isHigherBetter()) {
            setStartValue(mRange.getHigh());
        }
    }

    /**
     * A representation of a range, or interval, of values that can
     * be assigned to this score
     */
    public static class Range {

        /**
         * Constructor
         *
         * @param low  the lowest value possible in the range
         * @param high the highest value possible in the range
         */
        public Range(double low, double high) {
            mLow = low;
            mHigh = high;

            if (mLow >= mHigh) {
                throw new IllegalArgumentException("low isn't lower than high!");
            }
        }

        /**
         * Constructor.
         * Generates an instance of <code>Range</code> from the given <code>JSONObject</code>.
         *
         * @param jsonObject A JSONObject representation of the wanted <code>Range</code>.
         * @throws JSONException
         */
        public Range(JSONObject jsonObject) throws JSONException {
            mLow = jsonObject.getDouble(LUJSONConsts.LU_SCORE_RANGE_LOW);
            mHigh = jsonObject.getDouble(LUJSONConsts.LU_SCORE_RANGE_HIGH);

            if (mLow >= mHigh) {
                throw new IllegalArgumentException("low isn't lower than high!");
            }
        }

        /**
         * Converts the current <code>Range</code> to a JSONObject.
         *
         * @return A <code>JSONObject</code> representation of the current <code>Range</code>.
         */
        public JSONObject toJSONObject() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(LUJSONConsts.LU_SCORE_RANGE_LOW, mLow);
                jsonObject.put(LUJSONConsts.LU_SCORE_RANGE_HIGH, mHigh);
            } catch (JSONException e) {
                SoomlaUtils.LogError(TAG, "An error occurred while generating JSON object.");
            }

            return jsonObject;
        }


        /**
         * Setters and Getters
         */

        public double getLow() {
            return mLow;
        }

        public double getHigh() {
            return mHigh;
        }

        /**
         * Private Members *
         */

        private static final String TAG = "SOOMLA RangeNumberScore Range";

        private double mLow;
        private double mHigh;
    }


    /**
     * Private Members *
     */

    private static final String TAG = "SOOMLA RangeScore";

    private Range mRange;
}
