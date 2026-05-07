import { useEffect, useRef, useState } from "react";

export function ParticleCanvas({
  color,
  count = 50,
}: {
  color: string;
  count?: number;
}) {
  const ref = useRef<HTMLCanvasElement | null>(null);

  useEffect(() => {
    const canvas = ref.current;
    if (!canvas) return;
    const ctx = canvas.getContext("2d");
    if (!ctx) return;

    let raf = 0;
    let w = 0;
    let h = 0;
    const dpr = Math.min(window.devicePixelRatio || 1, 2);

    type P = { x: number; y: number; r: number; vy: number; vx: number; o: number };
    let particles: P[] = [];

    const resize = () => {
      const rect = canvas.getBoundingClientRect();
      w = rect.width || canvas.parentElement?.clientWidth || window.innerWidth;
      h = rect.height || canvas.parentElement?.clientHeight || window.innerHeight;
      canvas.width = Math.floor(w * dpr);
      canvas.height = Math.floor(h * dpr);
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    };

    const make = (atTop = false): P => ({
      x: Math.random() * w,
      y: atTop ? -10 : Math.random() * h,
      r: 0.5 + Math.random() * 2,
      vy: -(0.15 + Math.random() * 0.6),
      vx: (Math.random() - 0.5) * 0.25,
      o: 0.1 + Math.random() * 0.6,
    });

    const init = () => {
      particles = Array.from({ length: count }, () => make(false));
    };

    resize();
    init();

    const ro = new ResizeObserver(() => {
      resize();
      init();
    });
    ro.observe(canvas);
    if (canvas.parentElement) ro.observe(canvas.parentElement);
    // Re-init on next frame in case parent was 0-sized at mount
    const raf0 = requestAnimationFrame(() => { resize(); init(); });

    const tick = () => {
      ctx.clearRect(0, 0, w, h);
      for (const p of particles) {
        p.y += p.vy;
        p.x += p.vx;
        if (p.y < -5 || p.x < -10 || p.x > w + 10) {
          Object.assign(p, make(true));
        }
        ctx.beginPath();
        ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2);
        ctx.fillStyle = hexA(color, p.o);
        ctx.fill();
      }
      raf = requestAnimationFrame(tick);
    };
    raf = requestAnimationFrame(tick);

    return () => {
      cancelAnimationFrame(raf);
      cancelAnimationFrame(raf0);
      ro.disconnect();
    };
  }, [color, count]);

  return <canvas ref={ref} className="bw-particles" />;
}

function hexA(hex: string, a: number) {
  const h = hex.replace("#", "");
  const r = parseInt(h.slice(0, 2), 16);
  const g = parseInt(h.slice(2, 4), 16);
  const b = parseInt(h.slice(4, 6), 16);
  return `rgba(${r},${g},${b},${a})`;
}

export function FilmGrain() {
  return <div className="bw-grain" aria-hidden />;
}

export function ScanLine() {
  return <div className="bw-scanline" aria-hidden />;
}

export function CountUp({
  target,
  duration = 1400,
  className,
  delay = 0,
}: {
  target: number;
  duration?: number;
  className?: string;
  delay?: number;
}) {
  const [val, setVal] = useState(0);

  useEffect(() => {
    let raf = 0;
    let start = 0;
    let timer: number | undefined;
    const run = () => {
      const step = (t: number) => {
        if (!start) start = t;
        const p = Math.min(1, (t - start) / duration);
        const eased = 1 - Math.pow(1 - p, 3);
        setVal(Math.round(target * eased));
        if (p < 1) raf = requestAnimationFrame(step);
      };
      raf = requestAnimationFrame(step);
    };
    timer = window.setTimeout(run, delay);
    return () => {
      cancelAnimationFrame(raf);
      if (timer) clearTimeout(timer);
    };
  }, [target, duration, delay]);

  return <span className={className}>{val.toLocaleString()}</span>;
}

export function GlitchText({
  text,
  color = "#F5F0EB",
  size = "clamp(48px,8vw,90px)",
  shadow,
  className,
}: {
  text: string;
  color?: string;
  size?: string;
  shadow?: string;
  className?: string;
}) {
  const [active, setActive] = useState(false);
  useEffect(() => {
    const id = window.setInterval(() => {
      setActive(true);
      window.setTimeout(() => setActive(false), 500);
    }, 4000);
    return () => clearInterval(id);
  }, []);
  return (
    <span
      data-text={text}
      className={`bw-d bw-glitch ${active ? "bw-glitch-active" : ""} ${className ?? ""}`}
      style={{
        fontSize: size,
        color,
        textShadow: shadow,
      }}
    >
      {text}
    </span>
  );
}