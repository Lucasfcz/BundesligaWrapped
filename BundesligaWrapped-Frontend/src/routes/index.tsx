import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { Landing } from "@/components/wrapped/Landing";
import { Loading } from "@/components/wrapped/Loading";
import { SlideShow } from "@/components/wrapped/Slides";
import { MOCK, type WrappedData } from "@/lib/wrapped-mock";

export const Route = createFileRoute("/")({
  head: () => ({
    meta: [
      { title: "Bundesliga Wrapped — Your 2024/25 Season" },
      {
        name: "description",
        content:
          "Your personal Bundesliga 2024/25 recap. Goals, glory, and the moments that defined your season.",
      },
      { property: "og:title", content: "Bundesliga Wrapped — 2024/25" },
      {
        property: "og:description",
        content: "Your personal recap of the most beautiful league.",
      },
    ],
  }),
  component: Index,
});

type Stage = "landing" | "loading" | "slides";

function Index() {
  const [stage, setStage] = useState<Stage>("landing");
  const [data, setData] = useState<WrappedData | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [initialUserId, setInitialUserId] = useState("");

  // Pick up ?userId= from URL on mount
  useEffect(() => {
    if (typeof window === "undefined") return;
    const params = new URLSearchParams(window.location.search);
    const u = params.get("userId");
    if (u) {
      setInitialUserId(u);
      reveal(u);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const reveal = async (userId: string) => {
  setError(null);
  setStage("loading");
  const startedAt = Date.now();

  let result: WrappedData | null = null;

  try {
    const apiBaseUrl = import.meta.env.VITE_API_URL?.replace(/\/$/, "") ?? "";
    const res = await fetch(
      `${apiBaseUrl}/api/wrapped/${encodeURIComponent(userId)}`,
      { signal: AbortSignal.timeout(200000) }
    );

    if (res.ok) {
      const json = (await res.json()) as WrappedData;
      result = { ...json, userId: json.userId ?? userId };
    } else if (res.status === 404) {
      setError("Fan ID not found. Please check your ID and try again.");
    } else {
      setError(`Something went wrong (${res.status}). Please try again.`);
    }
  } catch (err) {
    const isTimeout = err instanceof Error && err.name === "TimeoutError";
    setError(
      isTimeout
        ? "The server is taking too long to respond. Please try again."
        : "Could not connect to the server. Please try again in a moment."
    );
  }

  const elapsed = Date.now() - startedAt;
  const wait = Math.max(0, 2800 - elapsed);

  window.setTimeout(() => {
    if (result) {
      setData(result);
      setStage("slides");
    } else {
      setStage("landing");
    }
  }, wait);
};

  const restart = () => {
    setStage("landing");
    setData(null);
    setError(null);
  };

  if (stage === "loading") return <Loading />;
  if (stage === "slides" && data) return <SlideShow data={data} onRestart={restart} />;
  return <Landing onReveal={reveal} initialUserId={initialUserId} error={error} />;
}
