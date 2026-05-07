import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { Landing } from "@/components/wrapped/Landing";
import { Loading } from "@/components/wrapped/Loading";
import { SlideShow } from "@/components/wrapped/Slides";
import { MOCK, type WrappedData } from "@/lib/wrapped-mock";

// Backend may send team/club fields as either a plain string or an object
// like { id, name }. Normalize everything to the shape the slides expect.
type ClubRef = string | { id?: string; name?: string } | null | undefined;

function refName(v: ClubRef): string {
  if (!v) return "";
  if (typeof v === "string") return v;
  return v.name ?? "";
}
function refId(v: ClubRef): string | undefined {
  if (!v || typeof v === "string") return undefined;
  return v.id;
}

function normalizeWrapped(raw: any, fallbackUserId: string): WrappedData {
  const sh = raw?.seasonHighlight ?? {};
  const fc = raw?.favoriteClub ?? {};
  const tp = raw?.topPlayer ?? {};
  const narrative = raw?.narrative;

  // Backend ClubSeasonStatsDTO uses goalsFor/goalsAgainst/biggestCrowd.
  // Frontend slide expects goalsScored. Map both shapes.
  const rawStats = fc.seasonStats;
  const seasonStats = rawStats
    ? {
        matchesPlayed: rawStats.matchesPlayed ?? 0,
        wins: rawStats.wins ?? 0,
        draws: rawStats.draws ?? 0,
        losses: rawStats.losses ?? 0,
        goalsScored: rawStats.goalsScored ?? rawStats.goalsFor ?? 0,
      }
    : undefined;

  return {
    ...raw,
    userId: raw?.userId ?? fallbackUserId,
    favoriteClub: {
      ...fc,
      clubName: refName(fc.clubName ?? fc.club) || fc.clubName || "",
      clubId: fc.clubId ?? refId(fc.club),
      seasonStats,
    },
    topPlayer: {
      ...tp,
      playerName: refName(tp.playerName) || tp.playerName || "",
      clubName: refName(tp.clubName ?? tp.club) || tp.clubName,
      clubId: tp.clubId ?? refId(tp.club),
    },
    seasonHighlight: {
      ...sh,
      homeTeam: refName(sh.homeTeam),
      guestTeam: refName(sh.guestTeam),
      homeTeamId: sh.homeTeamId ?? refId(sh.homeTeam),
      guestTeamId: sh.guestTeamId ?? refId(sh.guestTeam),
      result: typeof sh.result === "string" ? sh.result : String(sh.result ?? ""),
    },
    narrative: {
      text:
        typeof narrative === "string"
          ? narrative
          : narrative?.text ?? "",
    },
  };
}

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
      const json = await res.json();
      result = normalizeWrapped(json, userId);
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
