import os
import re
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib as mpl
import math

df = None

# method to extract dic with key folder name and value list of content
def get_dic_of_folders_with_files(data_folder):
    dic = {}
    for root, folders, files in os.walk(data_folder):
        for folder in folders:
            list_files = []
            current_folder = os.path.join(data_folder,folder)
            for root, folders, files in os.walk(current_folder):
                for file in files:
                    path = os.path.join(root,file)
                    with open(path) as inf:
                        list_files.append(inf.read())
            dic[folder] = list_files
    return dic

# method to extract values from a sigle file  
def extract_single_record_from_file (lines, graph_type):
    # Inputs
    # # get n value 
    n_value = re.search(pattern = "N value : [0-9]+", string = lines).group()
    store_n_value = re.search(pattern = "[0-9]+", string = n_value).group()
    # # get l value
    l_value = re.search(pattern = "L value: [0-9]+", string = lines).group()
    store_l_value = re.search(pattern = "[0-9]+", string = l_value).group()
    # # get i value
    i_value = re.search(pattern = "I value: [0-9]+.[0-9]+", string = lines).group()
    store_i_value = re.search(pattern = "[0-9]+.[0-9]+", string = i_value).group()
    # # get number of graphs value
    graphs_value = re.search(pattern = "Number of graphs: [0-9]+.[0-9]+", string = lines).group()
    store_graphs_value = re.search(pattern = "[0-9]+.[0-9]+", string = graphs_value).group()

    # Outputs
    # get n value output  
    n_value_out = re.search(pattern = "Output:\nN value: [0-9]+", string = lines).group()
    store_n_value_out = re.search(pattern = "[0-9]+", string = n_value).group()
    
    pattern_regEx_sci = "(?:0|[0-9]\d*)?(?:\.\d*)?(?:\d[eE][+\-]?\d+)"
    pattern_double = "[0-9]+.[0-9]+"
    try:
        # get number of rounds
        number_of_rounds = re.search(pattern = "Number of rounds: {}".format(pattern_regEx_sci), string = lines)
        store_number_of_rounds = re.search(pattern = pattern_regEx_sci, string = number_of_rounds.group(0)).group(0)
    
    except AttributeError:
        # get number of rounds
        number_of_rounds = re.search(pattern = "Number of rounds: {}".format(pattern_double), string = lines)
        store_number_of_rounds = re.search(pattern = pattern_double, string = number_of_rounds.group(0)).group(0)
    
    try:    
        # get time in milliseconds  
        milliseconds = re.search(pattern = "milliseconds: {}".format(pattern_regEx_sci), string = lines)
        store_milliseconds = re.search(pattern = pattern_regEx_sci, string = milliseconds.group(0)).group(0)
        
    except AttributeError:
#         # get time in milliseconds  
        milliseconds = re.search(pattern = "milliseconds: {}".format(pattern_double), string = lines)
        store_milliseconds = re.search(pattern = pattern_double, string = milliseconds.group(0)).group(0)

    return [graph_type, store_n_value, store_l_value, store_i_value,
            store_graphs_value, store_n_value_out, store_number_of_rounds, store_milliseconds]

# method to extract dataframe and clean it up
def get_df(dic, soft_by):
    # convert to list for easy feeding to dataframe 
    list_of_records = []
    for key in dic:
        for entry in dic.get(key):
            list_of_records.append(extract_single_record_from_file (entry, key))

    # construct dataframe and populate with records 
    sim_data_df = pd.DataFrame(np.array(list_of_records),
                            columns=['Topology', 'Expected N', 'L', 'I', 'Number of Graphs', 'N', 'Number of Rounds', 'Time Taken'])
    # convert all numeric to float
    list_of_columns = list(sim_data_df.columns)
    list_of_columns.remove('Topology')
    for col in list_of_columns:
        sim_data_df[col] = sim_data_df[col].astype(float)
    sim_data_df = sim_data_df.sort_values(by=soft_by)
    return sim_data_df

# method to plot n vs rounds for all topologies
def plot_data_from_folder(df, x_feature, y_feature, label, log_x=False, log_y=False, rounds_vs_n=False, rounds_vs_l=False):
    display(df)
    
    # get unique values in Topology column
    topology_set = set(df[label])
    
    # style lines
    mpl.rcParams['lines.linewidth'] = 2
    mpl.rcParams['lines.linestyle'] = '--'
    
    # plot figure and set size
    plt.figure(figsize=(20, 12))
    
    # plot details 
#     plt.title("ISCALA comparison plot", fontsize=18)
    plt.xlabel(x_feature, fontsize=20)
    plt.ylabel(y_feature, fontsize=20)
    plt.xticks(fontsize=16)
    plt.yticks(fontsize=16)
    
    for count, topology in enumerate(topology_set):
        selected_topology = df.loc[df[label] == topology]
        x = selected_topology[x_feature].tolist()
        y = selected_topology[y_feature].tolist()
        plt.plot(x, y, label=topology)
        if(log_x):
            plt. xscale("log")
        if(log_y):
            plt. yscale("log")
          
    #automate this part to take highest known n value
    if(rounds_vs_n):
        theoretical_line_list = generate_array_of_round_values_for_given_n(256)
        plt.plot(theoretical_line_list[0], theoretical_line_list[1], label="Theoretical")
        if(log_x):
            plt. xscale("log")
        if(log_y):
            plt. yscale("log")
            
    if(rounds_vs_l):
        theoretical_line_list = generate_array_of_round_values_for_given_l(16, 2)
        plt.plot(theoretical_line_list[0], theoretical_line_list[1], label="Theoretical")
        if(log_x):
            plt. xscale("log")
        if(log_y):
            plt. yscale("log")
    
    plt.grid()
    plt.legend(loc='best', prop={'size': 16})
    plt.show()
    return

def set_df(path, soft_by=['Topology', 'N']):
    # concat data folder
    data_folder = os.path.join(os.getcwd(), path)
    # extract dic with key folder name and value list of content
    dic = get_dic_of_folders_with_files(data_folder)
    # extract recordsa, construct dataframe and clean it up
    df =  get_df(dic, soft_by)
    return df
    
def check_data(df):
    # check data and datatypes
    display(df)
    display(df.info())
    return

# get theoretical round values
def get_theoretical_rounds_values(n=32, l=1, iso=1):
    output = (384 * (n**3) * (math.log(n)) * (math.log(n, 2)**2)) / (l * (2/n)**2)
    return output

# generate array of powers of 2
def generate_array_of_powers_of_2_values(n, start=4):
    list_of_n_values = [i for i in range(start, n+1) if (math.log(i)/math.log(2)).is_integer()]
    return list_of_n_values


def generate_array_of_round_values_for_given_n(n):
    list_of_n_values = generate_array_of_powers_of_2_values(n);
    list_of_round_values = [get_theoretical_rounds_values(i) for i in list_of_n_values]
    return np.array([list_of_n_values, list_of_round_values])


def generate_array_of_round_values_for_given_l(n, start):
    list_of_l_values = generate_array_of_powers_of_2_values(n, start);
    list_of_round_values = [get_theoretical_rounds_values(n=32, l=i, iso=1) for i in list_of_l_values]
    return np.array([list_of_l_values, list_of_round_values])