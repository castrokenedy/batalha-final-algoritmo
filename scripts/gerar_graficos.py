#!/usr/bin/env python3
"""Exporta 12 gráficos log-log (3 distribuições × 4 métricas), 300 DPI."""
from pathlib import Path
import argparse
import numpy as np
import pandas as pd
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

DISTRIBUICOES = ("ALEATORIO", "ORDENADO", "INVERSO")
METRICAS = {
    "tempo": ("media_tempo_ns", "Tempo (ms)", 1e6),
    "comparacoes": ("media_comparacoes", "Comparações entre valores", 1.0),
    "movimentacoes": ("media_movimentacoes", "Escritas em vetores", 1.0),
    "memoria": ("media_delta_heap_bytes", "Delta heap usado (bytes)", 1.0),
}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--csv", type=Path, default=Path("resultados/resultados_consolidados.csv"))
    parser.add_argument("--saida", type=Path, default=Path("graficos"))
    args = parser.parse_args()
    frame = pd.read_csv(args.csv)
    required = {"algoritmo", "tamanho", "distribuicao", *[v[0] for v in METRICAS.values()]}
    missing = required.difference(frame.columns)
    if missing:
        raise ValueError(f"Colunas CSV ausentes: {sorted(missing)}")
    args.saida.mkdir(parents=True, exist_ok=True)
    files = []
    for distribution in DISTRIBUICOES:
        subset = frame.loc[frame["distribuicao"] == distribution]
        if subset.empty:
            continue
        for metric, (column, ylabel, divisor) in METRICAS.items():
            fig, ax = plt.subplots(figsize=(10, 6))
            plotted = False
            for name, group in subset.groupby("algoritmo", sort=True):
                group = group.sort_values("tamanho")
                x = group["tamanho"].to_numpy(dtype=float)
                y = group[column].to_numpy(dtype=float) / divisor
                # Log(y) não existe para zero ou negativo: manter delta assinado no CSV,
                # não forçar epsilon fictício nem usar valor absoluto enganoso.
                valid = np.isfinite(x) & np.isfinite(y) & (x > 0) & (y > 0)
                if valid.any():
                    ax.plot(x[valid], y[valid], marker="o", linewidth=1.8, label=name)
                    plotted = True
            ax.set_xscale("log")
            ax.set_yscale("log")
            ax.set_title(f"{metric.capitalize()} — {distribution.lower()}")
            ax.set_xlabel("Tamanho do vetor (n)")
            ax.set_ylabel(ylabel)
            ax.grid(True, which="both", alpha=0.3)
            if plotted:
                ax.legend(fontsize=9)
            else:
                ax.text(0.5, 0.5, "Sem valores positivos mensuráveis",
                        ha="center", va="center", transform=ax.transAxes)
            if metric == "memoria":
                ax.text(0.01, -0.18,
                        "Delta de heap: valores ≤ 0 omitidos na escala log; não representa pico de memória.",
                        transform=ax.transAxes, fontsize=8)
            fig.tight_layout()
            destination = args.saida / f"{metric}_{distribution.lower()}.png"
            fig.savefig(destination, dpi=300, bbox_inches="tight")
            plt.close(fig)
            files.append(destination)
    print(f"Gerados {len(files)} gráficos em {args.saida.resolve()}")
    for path in files:
        print(path)


if __name__ == "__main__":
    main()
