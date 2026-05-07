import { useState } from "react";
import { ParticleCanvas, FilmGrain, ScanLine, GlitchText } from "./Effects";

export function Landing({
  onReveal,
  initialUserId = "",
  error,
}: {
  onReveal: (userId: string) => void;
  initialUserId?: string;
  error?: string | null;
}) {
  const [val, setVal] = useState(initialUserId);

  const submit = (e?: React.FormEvent) => {
    e?.preventDefault();
    const v = val.trim();
    if (!v) return;
    onReveal(v);
  };

  return (
    <div className="bw-landing bw-app">
      <div className="bw-landing-grid" />
      <ParticleCanvas color="#D4001A" count={60} />
      <div className="bw-corner-circle" />
      <div className="bw-ghost-word">BUNDESLIGA</div>
      <FilmGrain />
      <ScanLine />

      <div className="bw-landing-content">
        <div className="bw-badge">
          <span className="bw-badge-dot" />
          <span>2024 / 25 SEASON</span>
        </div>

        <div className="bw-hero-line">
          <GlitchText text="SEASON" size="clamp(88px,14vw,170px)" color="#F5F0EB" />
        </div>
        <div className="bw-hero-line">
          <GlitchText
            text="WRAPPED"
            size="clamp(88px,14vw,170px)"
            color="#D4001A"
            shadow="0 0 120px rgba(212,0,26,0.6)"
          />
        </div>

        <p className="bw-subtitle">
          Your personal recap of the most beautiful league. <br />
          Goals, glory, and the moments that defined your season.
        </p>

        <form className="bw-input-row" onSubmit={submit}>
          <input
            className="bw-input"
            placeholder="ENTER YOUR FAN ID"
            value={val}
            onChange={(e) => setVal(e.target.value)}
            spellCheck={false}
            autoComplete="off"
          />
          <button className="bw-btn-primary" type="submit" disabled={!val.trim()}>
            REVEAL →
          </button>
        </form>

        {error && <div className="bw-error">⚠ {error}</div>}
      </div>

      <div className="bw-ticker">
        <div className="bw-ticker-track">
          {Array.from({ length: 2 }).map((_, i) => (
            <span key={i}>
              BUNDESLIGA · WRAPPED · 2024/25 · DIE SCHÖNSTE LIGA · BUNDESLIGA · WRAPPED ·
              2024/25 · DIE SCHÖNSTE LIGA ·
            </span>
          ))}
        </div>
      </div>
    </div>
  );
}