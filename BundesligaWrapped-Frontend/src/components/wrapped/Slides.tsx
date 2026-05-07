import { useEffect, useRef, useState } from "react";
import html2canvas from "html2canvas";
import { ParticleCanvas, FilmGrain, ScanLine, CountUp } from "./Effects";
import { fanTypeMeta, MONTHS, type WrappedData } from "@/lib/wrapped-mock";
import { crestUrl, resolveTeamName, getClubLogo } from "@/lib/clubs";

export function SlideShow({
  data,
  onRestart,
}: {
  data: WrappedData;
  onRestart: () => void;
}) {
  const [idx, setIdx] = useState(0);
  const total = 6;
  const slideRef = useRef<HTMLDivElement | null>(null);

  const next = () => {
    if (idx === total - 1) onRestart();
    else setIdx((v) => Math.min(total - 1, v + 1));
  };
  const prev = () => setIdx((v) => Math.max(0, v - 1));

  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if (e.key === "ArrowRight") next();
      if (e.key === "ArrowLeft") prev();
    };
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [idx]);

  return (
    <div className="bw-stage bw-app">
      <div className="bw-progress">
        {Array.from({ length: total }).map((_, i) => (
          <div
            key={i}
            className={`bw-progress-seg ${i === idx ? "active" : i < idx ? "past" : ""}`}
            onClick={() => setIdx(i)}
          />
        ))}
      </div>

      <div className="bw-clickzone left" onClick={prev} />
      <div className="bw-clickzone right" onClick={next} />

      <div ref={slideRef} key={idx}>
        {idx === 0 && <Slide1 data={data} />}
        {idx === 1 && <Slide2 data={data} />}
        {idx === 2 && <Slide3 data={data} />}
        {idx === 3 && <Slide4 data={data} />}
        {idx === 4 && <Slide5 data={data} />}
        {idx === 5 && <Slide6 data={data} slideRef={slideRef} onRestart={onRestart} />}
      </div>

      <div className="bw-nav">
        <button className="bw-nav-arrow" onClick={prev} aria-label="Previous">
          ‹
        </button>
        <span className="bw-nav-count">
          {String(idx + 1).padStart(2, "0")} / {String(total).padStart(2, "0")}
        </span>
        <button
          className={`bw-nav-arrow ${idx === total - 1 ? "gold" : "next"}`}
          onClick={next}
          aria-label="Next"
        >
          {idx === total - 1 ? "↻" : "›"}
        </button>
      </div>
    </div>
  );
}

/* ---------------- Slide 1 ---------------- */
function Slide1({ data }: { data: WrappedData }) {
  const { fanIdentity } = data;
  const meta = fanTypeMeta(fanIdentity.fanType);
  return (
    <section className="bw-slide bw-s1">
      <ParticleCanvas color={meta.color} count={70} />
      <FilmGrain />
      <div className="bw-s1-glow" style={{ background: meta.color }} />
      <div className="bw-s1-ghost" style={{ color: `${meta.color}10` }}>
        {fanIdentity.fanType.toUpperCase()}
        <br />
        {fanIdentity.fanType.toUpperCase()}
      </div>

      <div className="bw-s1-content">
        <div className="bw-eyebrow a-fadeDown" style={{ animationDelay: "0.1s" }}>
          THIS SEASON, YOU WERE A
        </div>
        <div
          className="bw-s1-emoji a-emojiPop"
          style={{
            animationDelay: "0.2s",
            filter: `drop-shadow(0 0 30px ${meta.color})`,
          }}
        >
          {meta.emoji}
        </div>
        <div
          className="bw-s1-fantype a-numberSlam"
          style={{
            animationDelay: "0.35s",
            color: meta.color,
            textShadow: `0 0 80px ${meta.color}`,
          }}
        >
          {fanIdentity.fanType.toUpperCase()}
        </div>
        <div className="bw-s1-desc a-fadeUp" style={{ animationDelay: "0.5s" }}>
          {meta.desc}
        </div>

        <div className="bw-stat-grid a-fadeUp" style={{ animationDelay: "0.6s" }}>
          <Cell n={fanIdentity.articles} l="ARTICLES" />
          <Cell n={fanIdentity.videos} l="VIDEOS" />
          <Cell n={fanIdentity.stories} l="STORIES" />
          <Cell n={fanIdentity.matchCenter} l="MATCH CENTER" />
        </div>

        <div className="bw-s1-total a-numberSlam" style={{ animationDelay: "0.8s" }}>
          <CountUp target={fanIdentity.totalEngagements} duration={1600} delay={800} />
        </div>
        <div className="bw-s1-total-lbl">TOTAL ENGAGEMENTS</div>
      </div>
    </section>
  );
}
function Cell({ n, l }: { n: number; l: string }) {
  return (
    <div className="bw-stat-cell">
      <div className="bw-stat-num">
        <CountUp target={n} duration={1200} delay={700} />
      </div>
      <div className="bw-stat-lbl">{l}</div>
    </div>
  );
}

/* ---------------- Slide 2 ---------------- */
function Slide2({ data }: { data: WrappedData }) {
  const { mostActiveMonth } = data;
  const activeIdx = MONTHS.indexOf(mostActiveMonth.month.slice(0, 3).toUpperCase());
  return (
    <section className="bw-slide bw-s2">
      <ParticleCanvas color="#4A7FD4" count={40} />
      <div className="bw-s2-bar" />
      <div className="bw-s2-rules" />

      <div className="bw-s2-content">
        <div className="bw-eyebrow gold a-fadeDown" style={{ animationDelay: "0.1s" }}>
          ⚡ YOUR PEAK MATCHDAY MONTH
        </div>
        <div className="bw-month-row a-fadeUp" style={{ animationDelay: "0.3s" }}>
          {MONTHS.map((m, i) => (
            <div key={m} className={`bw-month-chip ${i === activeIdx ? "active" : ""}`}>
              {m}
            </div>
          ))}
        </div>
        <div className="bw-s2-month a-numberSlam" style={{ animationDelay: "0.5s" }}>
          {mostActiveMonth.month.toUpperCase()}
        </div>
        <div className="bw-s2-count a-numberSlam" style={{ animationDelay: "0.65s" }}>
          <CountUp target={mostActiveMonth.matchCenterCount} duration={1500} delay={650} />
        </div>
        <div className="bw-stat-lbl">MATCH CENTER VISITS</div>
        <div className="bw-flavor a-fadeIn" style={{ animationDelay: "0.9s" }}>
          The month you couldn't look away.
        </div>
      </div>
    </section>
  );
}

/* ---------------- Slide 3 ---------------- */
function Slide3({ data }: { data: WrappedData }) {
  const { favoriteClub } = data;
  const primary = favoriteClub.primaryColor || "#D4001A";
  const secondary = favoriteClub.secondaryColor || "#FFFFFF";
  const longName = favoriteClub.clubName;
  const stats = favoriteClub.seasonStats;
  const motto = favoriteClub.motto;
  const logo = favoriteClub.clubId ? getClubLogo(favoriteClub.clubId) : null;
  const [logoOk, setLogoOk] = useState(true);

  return (
    <section className="bw-slide bw-s3">
      <ParticleCanvas color={primary} count={55} />
      <FilmGrain />
      <div className="bw-s3-blob1" style={{ background: primary }} />
      <div className="bw-s3-blob2" style={{ background: secondary }} />
      <div className="bw-s3-stripes" />
      <div
        className="bw-s3-radial"
        style={{
          background: `radial-gradient(circle at 50% 45%, ${primary}22 0%, transparent 55%)`,
        }}
      />

      <div className="bw-s3-content">
        <div className="bw-eyebrow a-fadeDown" style={{ animationDelay: "0.1s" }}>
          YOUR CLUB THIS SEASON
        </div>

        <div className="bw-shield-wrap a-shieldDrop" style={{ animationDelay: "0.2s" }}>
          {logo && logoOk ? (
            <div
              className="bw-crest-glow"
              style={{
                filter: `drop-shadow(0 0 40px ${primary}) drop-shadow(0 0 90px ${primary}aa)`,
              }}
            >
              <img
                src={logo}
                alt={`${longName} crest`}
                className="bw-crest-img"
                onError={() => setLogoOk(false)}
              />
            </div>
          ) : (
            <Shield primary={primary} secondary={secondary} label={favoriteClub.shortName} />
          )}
        </div>

        <div className="bw-club-name a-numberSlam" style={{ animationDelay: "0.5s" }}>
          {longName}
        </div>

        {stats && (
          <div className="bw-trophy-row a-fadeUp" style={{ animationDelay: "0.7s" }}>
            <div className="bw-trophy-cell">
              <div className="bw-trophy-count">{stats.matchesPlayed}</div>
              <div className="bw-trophy-lbl">MATCHES</div>
            </div>
            <div className="bw-trophy-cell">
              <div className="bw-trophy-count" style={{ color: primary }}>
                {stats.wins}
              </div>
              <div className="bw-trophy-lbl">WINS</div>
            </div>
            <div className="bw-trophy-cell">
              <div className="bw-trophy-count">{stats.draws}</div>
              <div className="bw-trophy-lbl">DRAWS</div>
            </div>
            <div className="bw-trophy-cell">
              <div className="bw-trophy-count">{stats.losses}</div>
              <div className="bw-trophy-lbl">LOSSES</div>
            </div>
            <div className="bw-trophy-cell">
              <div className="bw-trophy-count" style={{ color: primary }}>
                {stats.goalsScored}
              </div>
              <div className="bw-trophy-lbl">GOALS</div>
            </div>
          </div>
        )}

        {motto && (
          <div className="bw-motto a-fadeIn" style={{ animationDelay: "0.95s" }}>
            <span className="bw-motto-native">“{motto.native}”</span>
            {motto.english && (
              <span className="bw-motto-en">
                <span className="bw-motto-q">“</span>
                {motto.english}
                <span className="bw-motto-q">”</span>
              </span>
            )}
          </div>
        )}

        <div className="bw-flavor a-fadeIn" style={{ animationDelay: "1.1s" }}>
          Through every matchday — your heart never left.
        </div>
      </div>
    </section>
  );
}

function Shield({
  primary,
  secondary,
  label,
}: {
  primary: string;
  secondary: string;
  label: string;
}) {
  return (
    <svg
      width="180"
      height="220"
      viewBox="0 0 180 220"
      style={{
        filter: `drop-shadow(0 0 50px ${primary}) drop-shadow(0 0 100px ${primary}60)`,
      }}
    >
      <defs>
        <linearGradient id="sg" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor={primary} stopOpacity="1" />
          <stop offset="100%" stopColor={primary} stopOpacity="0.7" />
        </linearGradient>
      </defs>
      <path
        d="M90 8 L170 32 L170 120 Q170 180 90 212 Q10 180 10 120 L10 32 Z"
        fill="url(#sg)"
        stroke={secondary}
        strokeWidth="3"
      />
      <path
        d="M90 28 L150 46 L150 118 Q150 168 90 192 Q30 168 30 118 L30 46 Z"
        fill="none"
        stroke={secondary}
        strokeWidth="1.5"
        opacity="0.6"
      />
      <text
        x="90"
        y="125"
        textAnchor="middle"
        fill={secondary}
        fontFamily="Bebas Neue, sans-serif"
        fontSize="32"
        letterSpacing="2"
      >
        {label.toUpperCase()}
      </text>
    </svg>
  );
}

/* ---------------- Slide 4 ---------------- */
function Slide4({ data }: { data: WrappedData }) {
  const { topPlayer } = data;
  const parts = topPlayer.playerName.split(" ");
  const last = parts[parts.length - 1];
  const first = parts.slice(0, -1).join(" ");
  const goalsPct = Math.min(100, (topPlayer.goals / 34) * 100);
  const playerClubLogo = getClubLogo(topPlayer.clubId);
  const playerClubName =
    topPlayer.clubName ||
    (topPlayer.clubId ? resolveTeamName(topPlayer.clubId) : "") ||
    topPlayer.clubShortName ||
    "";

  return (
    <section className="bw-slide bw-s4">
      <ParticleCanvas color="#C9A84C" count={45} />
      <div className="bw-s4-glow" />
      <div className="bw-s4-stripe" />
      <div className="bw-s4-ghostnum">{topPlayer.goals}</div>

      <div className="bw-s4-content">
        <div className="bw-eyebrow gold a-fadeDown" style={{ animationDelay: "0.1s" }}>
          ⚽ YOUR PLAYER OF THE SEASON
        </div>

        <div className="bw-player-first a-fadeUp" style={{ animationDelay: "0.2s" }}>
          {first || "—"}
        </div>
        <div className="bw-player-last a-numberSlam" style={{ animationDelay: "0.3s" }}>
          {last}
        </div>

        {playerClubName && (
          <div className="bw-player-club a-fadeIn" style={{ animationDelay: "0.4s" }}>
            {playerClubLogo && (
              <img
                src={playerClubLogo}
                alt={playerClubName}
                className="bw-player-club-logo"
                onError={(e) => ((e.currentTarget.style.display = "none"))}
              />
            )}
            <span className="bw-player-club-name">{playerClubName}</span>
          </div>
        )}

        <div className="bw-s4-rule" />

        <div className="bw-stats-row a-fadeUp" style={{ animationDelay: "0.5s" }}>
          <div className="bw-stat-block">
            <div className="bw-stat-icon">⚽</div>
            <div className="bw-stat-big red">
              <CountUp target={topPlayer.goals} duration={1400} delay={600} />
            </div>
            <div className="bw-stat-lbl">GOALS</div>
          </div>
          <div className="bw-stat-divider" />
          <div className="bw-stat-block">
            <div className="bw-stat-icon">🎯</div>
            <div className="bw-stat-big gold">
              <CountUp target={topPlayer.assists} duration={1400} delay={750} />
            </div>
            <div className="bw-stat-lbl">ASSISTS</div>
          </div>
        </div>

        <div className="bw-progress-bar-wrap">
          <div className="bw-progress-bar-bg">
            <div className="bw-progress-bar-fill" style={{ width: `${goalsPct}%` }} />
          </div>
          <div className="bw-progress-bar-lbl">
            <span>0</span>
            <span>OUT OF 34 MATCHDAYS</span>
            <span>34</span>
          </div>
        </div>

        <div className="bw-flavor a-fadeIn" style={{ animationDelay: "1.2s" }}>
          A goal nearly every weekend. Pure cold-blooded class.
        </div>
      </div>
    </section>
  );
}

/* ---------------- Slide 5 ---------------- */
function Slide5({ data }: { data: WrappedData }) {
  const { seasonHighlight, favoriteClub } = data;
  const [home, away] = seasonHighlight.result.split(":").map((s) => s.trim());

  const homeName = resolveTeamName(seasonHighlight.homeTeam, seasonHighlight.homeTeamId);
  const guestName = resolveTeamName(seasonHighlight.guestTeam, seasonHighlight.guestTeamId);

  // Discover whether favorite club played at home or away.
  const isFavoriteHome =
    homeName === favoriteClub.clubName || seasonHighlight.homeTeamId === favoriteClub.clubId;

  const favoriteScore = isFavoriteHome ? home : away;
  const opponentScore = isFavoriteHome ? away : home;
  const opponentName = isFavoriteHome ? guestName : homeName;
  const opponentId = isFavoriteHome ? seasonHighlight.guestTeamId : seasonHighlight.homeTeamId;

  const favLogo = getClubLogo(favoriteClub.clubId);
  const oppLogo = getClubLogo(opponentId);

  return (
    <section className="bw-slide bw-s5">
      <ParticleCanvas color={favoriteClub.primaryColor || "#D4001A"} count={65} />
      <div className="bw-s5-floods" />
      <div className="bw-s5-scan" />
      <div className="bw-s5-vignette" />
      <ScanLine />

      <div className="bw-s5-content">
        <div className="bw-eyebrow gold a-fadeDown" style={{ animationDelay: "0.1s" }}>
          ⚡ SEASON'S BIGGEST MATCH
        </div>

        <div className="bw-matchup a-fadeUp" style={{ animationDelay: "0.25s" }}>
          <div className="bw-team home">
            {favLogo && (
              <img
                src={favLogo}
                alt={favoriteClub.clubName}
                className="bw-team-logo"
                onError={(e) => ((e.currentTarget.style.display = "none"))}
              />
            )}
            <span className="bw-team-name">{favoriteClub.clubName}</span>
          </div>
          <div className="bw-score-block">
            <span className="bw-score a-scoreSlam" style={{ animationDelay: "0.4s" }}>
              {favoriteScore}
            </span>
            <span className="bw-score-colon">:</span>
            <span className="bw-score a-scoreSlam" style={{ animationDelay: "0.55s" }}>
              {opponentScore}
            </span>
          </div>
          <div className="bw-team away">
            {oppLogo && (
              <img
                src={oppLogo}
                alt={opponentName}
                className="bw-team-logo"
                onError={(e) => ((e.currentTarget.style.display = "none"))}
              />
            )}
            <span className="bw-team-name">{opponentName}</span>
          </div>
        </div>

        <div className="bw-pills a-fadeUp" style={{ animationDelay: "0.6s" }}>
          <div className="bw-pill">
            <div className="bw-pill-val">
              🏟️{" "}
              <CountUp target={seasonHighlight.spectators} duration={1800} delay={700} />
            </div>
            <div className="bw-pill-lbl">FANS PRESENT</div>
          </div>
          <div className="bw-pill">
            <div className="bw-pill-val">📅 {seasonHighlight.matchDay}</div>
            <div className="bw-pill-lbl">MATCHDAY</div>
          </div>
        </div>

        <div className="bw-flavor a-fadeIn" style={{ animationDelay: "1s" }}>
          The night the stadium held its breath — and exhaled in red.
        </div>
      </div>

      <div className="bw-ticker">
        <div className="bw-ticker-track">
          {Array.from({ length: 2 }).map((_, i) => (
            <span key={i}>
              {favoriteClub.clubName} {favoriteScore}:{opponentScore} {opponentName} ·
              MATCHDAY {seasonHighlight.matchDay} · BUNDESLIGA 2024/25 ·
            </span>
          ))}
        </div>
      </div>
    </section>
  );
}

/* ---------------- Slide 6 ---------------- */
function Slide6({
  data,
  slideRef,
  onRestart,
}: {
  data: WrappedData;
  slideRef: React.RefObject<HTMLDivElement | null>;
  onRestart: () => void;
}) {
  const [copied, setCopied] = useState(false);

  const save = async () => {
    const node = slideRef.current;
    if (!node) return;
    const canvas = await html2canvas(node, {
      backgroundColor: "#030508",
      scale: 2,
      useCORS: true,
    });
    const link = document.createElement("a");
    link.download = `bundesliga-wrapped-${data.userId.slice(0, 8)}.png`;
    link.href = canvas.toDataURL("image/png");
    link.click();
  };

  const share = async () => {
    const url = `${window.location.origin}?userId=${data.userId}`;
    try {
      await navigator.clipboard.writeText(url);
      setCopied(true);
      window.setTimeout(() => setCopied(false), 2000);
    } catch {
      setCopied(false);
    }
  };

  return (
    <section className="bw-slide bw-s6">
      <ParticleCanvas color="#C9A84C" count={35} />
      <div className="bw-s6-grid" />
      <div className="bw-s6-glow" />

      <div className="bw-s6-content">
        <div className="bw-eyebrow gold a-fadeDown" style={{ animationDelay: "0.1s" }}>
          YOUR 2024 / 25 STORY
        </div>

        <div className="bw-quote a-fadeUp" style={{ animationDelay: "0.3s" }}>
          <span className="bw-quote-mark open">“</span>
          <p className="bw-quote-text">{data.narrative.text}</p>
          <span className="bw-quote-mark close">”</span>
        </div>

        <div className="bw-divider a-fadeIn" style={{ animationDelay: "0.55s" }} />

        <div className="bw-wrapped-badge a-fadeUp" style={{ animationDelay: "0.65s" }}>
          <span>⚽</span>
          <span className="lbl1">BUNDESLIGA WRAPPED</span>
          <span className="lbl2">2024 / 25 SEASON</span>
        </div>

        <div className="bw-fanid a-fadeIn" style={{ animationDelay: "0.75s" }}>
          FAN ID · {data.userId}
        </div>

        <div className="bw-share-row a-fadeUp" style={{ animationDelay: "0.9s" }}>
          <button className="bw-share-btn red" onClick={save}>
            💾 SAVE IMAGE
          </button>
          <button className="bw-share-btn ghost" onClick={share}>
            {copied ? "✅ COPIED!" : "🔗 SHARE LINK"}
          </button>
          <button className="bw-share-btn ghost" onClick={onRestart}>
            ↻ RESTART
          </button>
        </div>
      </div>
    </section>
  );
}

// crestUrl is re-exported indirectly via getClubLogo; keep import to avoid tree-shake surprises.
void crestUrl;