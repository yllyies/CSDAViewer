import numpy as np
import pandas as pd
from sklearn.cluster import KMeans
from sklearn.metrics import silhouette_score

if __name__ == '__main__':
    df = pd.read_excel('C:/Users/michaeli/Downloads/test.xlsx', usecols=[1])
    df_li = df.values
    result = []
    for s_li in df_li:
        result.append(s_li[0])
    x = np.array(result).reshape(-1, 1)

    # 定义候选的K值。
    scope = range(2, 5)
    # 定义SSE列表，用来存放不同K值下的SSE。
    #-----------------------------轮廓系数法确定k值（3）---------------
    sil_score = []
    n_clusters = 2
    for k in range(2,6):
        kmeans = KMeans(n_init='auto', n_clusters=k,random_state=0).fit(x)
        value = silhouette_score(x, kmeans.labels_)
        sil_score.append(value)
        if value > 0.95 and n_clusters == 2:
            n_clusters = k
    print(n_clusters)
    model = KMeans(n_init="auto", n_clusters=n_clusters, max_iter=100)
    model.fit(x)
    # 质心数值，确定档位
    c = np.sort(model.cluster_centers_.round().flatten())
    print(c)
    # 0 则为 关机; min() 为待机; max() 为运行 按照顺序 index=0 关机，index=1待机，index=2低功率 index=3
    stand_by_power = np.min(c).round(0)
    run_power = np.max(c).round(0)

