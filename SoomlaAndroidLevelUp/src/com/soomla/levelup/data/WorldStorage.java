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

package com.soomla.levelup.data;

import android.text.TextUtils;

import com.soomla.BusProvider;
import com.soomla.data.KeyValueStorage;
import com.soomla.levelup.LevelUp;
import com.soomla.levelup.World;
import com.soomla.levelup.events.WorldAssignedRewardEvent;
import com.soomla.levelup.events.WorldCompletedEvent;

/**
 * Created by refaelos on 13/05/14.
 */
public class WorldStorage {

    private static String keyWorlds(String worldId, String postfix) {
        return LevelUp.DB_KEY_PREFIX + "worlds." + worldId + "." + postfix;
    }

    private static String keyWorldCompleted(String worldId) {
        return keyWorlds(worldId, "completed");
    }

    private static String keyReward(String worldId) {
        return keyWorlds(worldId, "assignedReward");
    }


    public static void setCompleted(World world, boolean completed) {
        setCompleted(world, completed, true);
    }

    public static void setCompleted(World world, boolean completed, boolean notify) {

        boolean currentStatus = isCompleted(world);
        if (currentStatus == completed) {

            // we don't need to set the status of a world to the same status over and over again.
            // couldn't only cause trouble.
            return;
        }

        String worldId = world.getID();
        String key = keyWorldCompleted(worldId);

        if (completed) {
            KeyValueStorage.setValue(key, "yes");

            if (notify) {
                BusProvider.getInstance().post(new WorldCompletedEvent(world));
            }
        } else {
            KeyValueStorage.deleteKeyValue(key);
        }
    }

    public static boolean isCompleted(World world) {
        String key = keyWorldCompleted(world.getID());
        String val = KeyValueStorage.getValue(key);
        return !TextUtils.isEmpty(val);
    }


    /**
     * World Reward *
     */

    public static void setReward(World world, String rewardId) {

        String key = keyReward(world.getID());
        if (!TextUtils.isEmpty(rewardId)) {
            KeyValueStorage.setValue(key, rewardId);
        } else {
            KeyValueStorage.deleteKeyValue(key);
        }

        // Notify world was assigned a reward
        BusProvider.getInstance().post(new WorldAssignedRewardEvent(world));
    }

    public static String getAssignedReward(World world) {
        String key = keyReward(world.getID());
        return KeyValueStorage.getValue(key);
    }
}
