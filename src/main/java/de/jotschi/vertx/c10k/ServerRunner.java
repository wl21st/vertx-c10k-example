package de.jotschi.vertx.c10k;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class ServerRunner {

	public static void main(String[] args) {
		VertxOptions vertxOptions = new VertxOptions();
		vertxOptions.setPreferNativeTransport(true);

		Vertx vertx = Vertx.vertx(vertxOptions);

		if (vertx.isNativeTransportEnabled()) {
			System.out.println("Native transports have been enabled.");
		} else {
			System.err.println("Native transports have not been enabled. Maybe you are not running this on x86_64 linux");
			System.err.println("Stopping server..");
			System.exit(10);
		}

		DeploymentOptions options = new DeploymentOptions();
		int nVerticles = Runtime.getRuntime().availableProcessors();
		System.out.println("Deploying {" + nVerticles + "} verticles");
		options.setInstances(nVerticles);
		vertx.deployVerticle(ServerVerticle.class, options, ch -> {
			if (ch.failed()) {
				ch.cause().printStackTrace();
			} else {
				System.out.println("Server verticles deployed.");
			}
		});

	}

}
