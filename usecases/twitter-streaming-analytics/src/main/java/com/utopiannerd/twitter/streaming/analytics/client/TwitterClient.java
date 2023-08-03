package com.utopiannerd.twitter.streaming.analytics.client;

import static com.twitter.hbc.core.Constants.STREAM_HOST;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.CLIENT_NAME;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.CONSUMER_KEYS;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.CONSUMER_SECRETS;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.SECRET;
import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.TOKEN;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/** This class is used to get the tweet data from Twitter using the hbc-core library. */
public final class TwitterClient {

  private final Client twitterClient;

  public TwitterClient(BlockingQueue<String> blockingQueue, List<String> targetTrackTerms) {
    this.twitterClient = createClient(blockingQueue, targetTrackTerms);
  }

  public Client getTwitterClient() {
    if (this.twitterClient == null) {
      throw new IllegalStateException("twitterClient cannot be null or empty");
    }
    return this.twitterClient;
  }

  public Client createClient(BlockingQueue<String> blockingQueue, List<String> targetTrackTerms) {

    Hosts hoseBirdHosts = new HttpHosts(STREAM_HOST);

    StatusesFilterEndpoint hoseBirdEndpoint = new StatusesFilterEndpoint();

    // Configuring the endpoint to fetch tweets for the search terms.
    hoseBirdEndpoint.trackTerms(targetTrackTerms);

    // Twitter credentials configuration.
    Authentication hoseBirdAuth = new OAuth1(CONSUMER_KEYS, CONSUMER_SECRETS, TOKEN, SECRET);

    // Building the client.
    Client twitterClient =
        new ClientBuilder()
            .name(CLIENT_NAME)
            .hosts(hoseBirdHosts)
            .authentication(hoseBirdAuth)
            .endpoint(hoseBirdEndpoint)
            .processor(new StringDelimitedProcessor(blockingQueue))
            .build();

    return twitterClient;
  }
}
