.PHONY: test_obj_exe_run test_obj_cuda_run clean

GCC = g++
NVCC = nvcc
CUDA_FLAGS = -arch=sm_86

test_obj_exe_run:
					$(GCC) -c Node.cpp ISCALA.cpp DFS.cpp ER_Graph.cpp Read_Graph.cpp Write_Graph.cpp Driver.cpp
					$(GCC) -o Driver Node.o ISCALA.o DFS.o ER_Graph.o Read_Graph.o Write_Graph.o Driver.o
					./Driver

test_obj_cuda_run: 
					$(NVCC) $(CUDA_FLAGS) -o DriverCuda Driver.cu
					./DriverCuda

clean:
		rm -f *.exe *.cu *.o Driver DriverCuda