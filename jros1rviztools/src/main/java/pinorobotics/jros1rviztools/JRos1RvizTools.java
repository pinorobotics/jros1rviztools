/*
 * Copyright 2021 jrosrviztools project
 * 
 * Website: https://github.com/pinorobotics/jros1rviztools
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pinorobotics.jros1rviztools;

import id.jros1messages.std_msgs.HeaderMessage;
import id.jros1messages.visualization_msgs.MarkerArrayMessage;
import id.jros1messages.visualization_msgs.MarkerMessage;
import id.jros1messages.visualization_msgs.MarkerMessage.Action;
import id.jros1messages.visualization_msgs.MarkerMessage.Type;
import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.primitives.Duration;
import id.jrosmessages.primitives.Time;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.lang.XThread;
import id.xfunction.logging.XLogger;
import java.io.IOException;
import pinorobotics.jros1rviztools.impl.Transformer;
import pinorobotics.jrosrviztools.JRosRvizTools;
import pinorobotics.jrosrviztools.entities.Color;
import pinorobotics.jrosrviztools.entities.MarkerType;
import pinorobotics.jrosrviztools.entities.Point;
import pinorobotics.jrosrviztools.entities.Pose;
import pinorobotics.jrosrviztools.entities.Vector3;

/**
 * ROS1 implementation set of methods to work with RViz
 *
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public class JRos1RvizTools implements JRosRvizTools {

    private static final XLogger LOGGER = XLogger.getLogger(JRos1RvizTools.class);
    private static final QuaternionMessage ORIENTATION = new QuaternionMessage().withW(1.0);
    private TopicSubmissionPublisher<MarkerArrayMessage> markerPublisher;
    private boolean markerPublisherActive;
    private JRosClient client;
    private String baseFrame;
    private volatile int nsCounter;
    private Transformer transformer = new Transformer();

    JRos1RvizTools(JRosClient client, String baseFrame, String topic) {
        this.client = client;
        this.baseFrame = baseFrame;
        markerPublisher = new TopicSubmissionPublisher<>(MarkerArrayMessage.class, topic);
    }

    /** Send text message to RViz which will be displayed at the given position. */
    @Override
    public void publishText(Color color, Vector3 scale, Pose pose, String text) throws Exception {
        LOGGER.entering("publishText");
        if (!markerPublisherActive) {
            client.publish(markerPublisher);
            markerPublisherActive = true;
        }
        publish(
                new MarkerMessage()
                        .withHeader(createHeader())
                        .withNs(new StringMessage(nextNameSpace()))
                        .withType(Type.TEXT_VIEW_FACING)
                        .withAction(Action.ADD)
                        .withText(new StringMessage().withData(text))
                        .withPose(transformer.toPoseMessage(pose).withQuaternion(ORIENTATION))
                        .withColor(transformer.toColorRGBMessage(color))
                        .withScale(transformer.toVector3Message(scale))
                        .withLifetime(Duration.UNLIMITED));
        LOGGER.exiting("publishText");
    }

    /**
     * Publish new marker to RViz
     *
     * @param points Points with coordinates which describe marker position in space
     */
    @Override
    public void publishMarkers(Color color, Vector3 scale, MarkerType markerType, Point... points)
            throws Exception {
        LOGGER.entering("publishMarker");
        if (!markerPublisherActive) {
            client.publish(markerPublisher);
            markerPublisherActive = true;
        }
        var markers = new MarkerMessage[points.length];
        for (int i = 0; i < markers.length; i++) {
            markers[i] =
                    new MarkerMessage()
                            .withHeader(createHeader())
                            .withNs(new StringMessage(nextNameSpace()))
                            .withType(transformer.toMarkerType(markerType))
                            .withAction(Action.ADD)
                            .withPose(
                                    new PoseMessage()
                                            .withPosition(transformer.toPointMessage(points[i]))
                                            .withQuaternion(new QuaternionMessage().withW(1.0)))
                            .withScale(transformer.toVector3Message(scale))
                            .withColor(transformer.toColorRGBMessage(color))
                            .withLifetime(Duration.UNLIMITED);
        }
        publish(markers);
        LOGGER.exiting("publishMarker");
    }

    private String nextNameSpace() {
        return "@" + hashCode() + "." + nsCounter++;
    }

    @Override
    public void close() throws IOException {
        LOGGER.entering("close");
        if (markerPublisherActive) {
            markerPublisher.close();
            client.unpublish(markerPublisher.getTopic());
        }
        markerPublisherActive = false;
        LOGGER.exiting("close");
    }

    private void publish(MarkerMessage... markers) {
        var message = new MarkerArrayMessage().withMarkers(markers);
        while (markerPublisher.getNumberOfSubscribers() == 0) {
            LOGGER.fine("No subscribers");
            XThread.sleep(100);
        }
        markerPublisher.submit(message);
    }

    private HeaderMessage createHeader() {
        return new HeaderMessage().withFrameId(baseFrame).withStamp(Time.now());
    }
}
